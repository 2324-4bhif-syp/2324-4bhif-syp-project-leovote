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

import java.util.List;
import java.util.UUID;

@ResourceProperties(path = "voters")
public interface VoterResource extends PanacheRepositoryResource<VoterRepository, Voter, Long> {
    VoterRepository voterRepository = CDI.current().select(VoterRepository.class).get();

    @POST
    @Path("/vote/{electionId}/{candidateId}")
    @Consumes("application/json")
    @Produces("application/json")
    default Response vote(
            @PathParam("electionId") Long electionId,
            @PathParam("candidateId") Long candidateId,
            @HeaderParam("voterId") UUID voterId
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

    @POST
    @Path("/elecetion/{electionId}")
    @Consumes("application/json")
    @Produces("application/json")
    default Response createVoters(
            @PathParam("electionId") Long electionId,
            @HeaderParam("voterCount") int voterCount
    ) {
        Election election = Election.findById(electionId);

        List<Voter> voters = voterRepository.createVotersForElection(voterCount, election);

        if (!voters.isEmpty()) {
            return Response.status(Response.Status.CREATED).entity(voters).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
