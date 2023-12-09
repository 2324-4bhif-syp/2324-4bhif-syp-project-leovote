package at.htlleonding.boundary;

import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@ResourceProperties(path = "voters")
public interface VoterResource extends PanacheRepositoryResource<VoterRepository, Voter, Long> {
    VoterRepository voterRepository = CDI.current().select(VoterRepository.class).get();

    @GET
    @Path("/vote/{electionId}/{candidateId}")
    @Produces("application/json")
    default Response vote(
            @PathParam("electionId") Long electionId,
            @PathParam("candidateId") Long candidateId,
            @HeaderParam("voterId") Long voterId
    ) {
        Election election = Election.findById(electionId);
        Candidate candidate = Candidate.findById(candidateId);
        Voter voter = Voter.findById(voterId);

        boolean voteIsValid = voterRepository.voteForCandidate(voter, candidate, election);

        if (voteIsValid) {
            return Response.accepted().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
