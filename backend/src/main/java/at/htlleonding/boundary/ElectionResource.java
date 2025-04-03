package at.htlleonding.boundary;

import at.htlleonding.control.AuthorizationService;
import at.htlleonding.control.ElectionRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Email;
import at.htlleonding.entity.dto.EmailDTO;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static at.htlleonding.boundary.Roles.ADMIN_ROLE;
import static at.htlleonding.boundary.Roles.USER_ROLE;

@Path("elections")
public class ElectionResource {
    @Inject
    ElectionRepository electionRepository;

    @Inject
    AuthorizationService authorizationService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElections() {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE) && !authorizationService.hasAccess(jwt, USER_ROLE)) {
            return Response.status(403).build();
        }
        // return all Elections
        List<Election> elections = Election.listAll();
        return Response.ok(elections).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElectionById(@PathParam("id") Long id) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE) && !authorizationService.hasAccess(jwt, USER_ROLE)) {
            return Response.status(403).build();
        }

        Election election = Election.findById(id);
        if (election == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(election).build();
    }

    @GET
    @Path("results/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response resultsById(
            @PathParam("electionId") Long electionId
    ) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

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
    public Response getEmailPerElection(@PathParam("electionId") Long electionId) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        List<String> mails = electionRepository.getVotersEmails(electionId);
        return Response.ok(mails).build();
    }

    @POST
    @Path("addEmail/{electionId}/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addEmailToElection(@PathParam("electionId") Long electionId, @PathParam("email") String email) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

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
    public Response addMultipleEmails(@PathParam("electionId") Long electionId, List<String> emails) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

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
    public Response addElection(Election election) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        Election got = electionRepository.createNewElection(election);
        return Response.accepted(got).build();
    }

    @DELETE
    @Path("removeEmail/{emailId}")
    @Transactional
    public Response removeEmailFromElection(@PathParam("emailId") Long email) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        if (email == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id must not be null").build();
        }

        electionRepository.removeEmailFromElection(email);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteElection(@PathParam("id") Long id) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id must not be null").build();
        }

        Election election = Election
                .findById(id);

        if (election == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        electionRepository.deleteElection(election);
        return Response.ok().build();
    }
}

