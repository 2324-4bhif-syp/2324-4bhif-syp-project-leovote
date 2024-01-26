package at.htlleonding.boundary;

import at.htlleonding.control.EmailService;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Email;
import at.htlleonding.entity.dto.EmailDTO;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/email")
public class EmailResource {
    @Inject
    EmailService emailService;

    @Inject
    EntityManager em;

    @Transactional
    @Path("/election/{electionId}")
    @Blocking
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Uni<Void> sendInvite(@PathParam("electionId") Long electionId) {
        Optional<Election> election = Election.findByIdOptional(electionId);

        if (election.isEmpty()) {
            return Uni.createFrom().failure(new IllegalArgumentException("Election with id " + electionId + " not found"));
        }

        // Perform email sending logic in a background task
        emailService.sendInvitations(election.get());

        // Return a response immediately
        return Uni.createFrom().voidItem();
    }

    @GET
    @Path("/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response emailsByElection(@PathParam("electionId") Long electionId){

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
