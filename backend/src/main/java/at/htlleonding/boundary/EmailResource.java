package at.htlleonding.boundary;

import at.htlleonding.control.EmailService;
import at.htlleonding.entity.Election;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;

@Path("/email")
public class EmailResource {
    @Inject
    EmailService emailService;

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
}
