package at.htlleonding.boundary;

import at.htlleonding.control.ElectionRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Email;
import at.htlleonding.entity.dto.EmailDTO;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ResourceProperties(path = "elections")
public interface ElectionResource extends PanacheRepositoryResource<ElectionRepository, Election, Long> {
    ElectionRepository electionRepository = CDI.current().select(ElectionRepository.class).get();

    @GET
    @Path("results/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response resultsById(
            @PathParam("electionId") Long electionId
    ) {
        Election election = Election.findById(electionId);

        if (election == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        HashMap<Candidate, Double> results;
        try {
            results = electionRepository.reviewResults(election);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Response.accepted(results).build();
    }

    @GET
    @Path("emails/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getEmailPerElection(@PathParam("electionId") Long electionId) {
        List<String> mails = electionRepository.getVotersEmails(electionId);
        return Response.ok(mails).build();
    }

    @POST
    @Path("addEmail/{electionId}/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response addEmailToElection(@PathParam("electionId") Long electionId, @PathParam("email") String email) {
        Optional<Email> returnedMail = electionRepository.addEmailtoElection(electionId, email);
        if (returnedMail.isPresent()) {
            EmailDTO emailDTO = new EmailDTO(returnedMail.get().getEmail(),
                    returnedMail.get().id,
                    returnedMail.get().getElection().id);

            return Response.accepted(emailDTO).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    @POST
    @Path("addEmail/multiples/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    default Response addMultipleEmails(@PathParam("electionId") Long electionId, List<String> emails) {
        Optional<Election> election = Election.findByIdOptional(electionId);

        if (election.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        emails.forEach(email -> {
            Email e = new Email(email, election.get());
            CDI.current().select(EntityManager.class).get().persist(e);
        });

        return Response.ok().build();
    }

    @POST
    @Path("election")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response addElection(Election election) {
        Election got = electionRepository.createNewElection(election);
        return Response.accepted(got).build();
    }

    @DELETE
    @Path("removeEmail/{emailId}")
    @Transactional
    default Response removeEmailFromElection(@PathParam("emailId") Long email) {
        electionRepository.removeEmailFromElection(email);
        return Response.noContent().build();
    }
}

