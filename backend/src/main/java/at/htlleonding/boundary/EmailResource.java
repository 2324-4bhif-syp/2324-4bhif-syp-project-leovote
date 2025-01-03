package at.htlleonding.boundary;

import at.htlleonding.control.EmailService;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Email;
import at.htlleonding.entity.dto.EmailDTO;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("email")
public class EmailResource {
    @Inject
    EmailService emailService;
    @Inject
    EntityManager em;
    @Inject
    Tracer tracer;

    @POST
    @Blocking
    @Transactional
    @Path("election/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> sendInvite(@PathParam("electionId") Long electionId) {
        Span span = tracer.spanBuilder("sendEmails").startSpan();

        Optional<Election> election = Election.findByIdOptional(electionId);

        if (election.isEmpty()) {
            return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).build());
        }

        try (Scope scope = span.makeCurrent()) {

            // Perform email sending logic in a background task
            emailService.sendInvitations(election.get()).subscribe().with(
                    success -> System.out.println("Emails sent successfully"),
                    failure -> System.out.println("Emails could not be sent\n" + failure.toString())
            );
        } finally {
            span.end();
        }

        return Uni.createFrom().item(Response.ok().entity("{\"message\": \"Emails are being sent asynchronously.\"}").build());
    }

    @GET
    @Path("{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response emailsByElection(@PathParam("electionId") Long electionId) {

        // Should be capsuled outside the resource
        TypedQuery<Email> query = em.createQuery("select e from Email e where e.election.id = :electionId", Email.class);
        query.setParameter("electionId", electionId);
        List<Email> emails = query.getResultList();

        List<EmailDTO> emailDTOS = emails.stream().map(email ->
                        new EmailDTO(email.getEmail(), email.id, email.getElection().id))
                .toList();

        return Response.ok(emailDTOS).build();
    }
}
