package at.htlleonding.boundary;

import at.htlleonding.control.Blockchain;
import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import at.htlleonding.entity.dto.VoterDTO;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ResourceProperties(path = "voters")
public interface VoterResource extends PanacheRepositoryResource<VoterRepository, Voter, Long> {
    VoterRepository voterRepository = CDI.current().select(VoterRepository.class).get();
    EntityManager em = voterRepository.getEntityManager();

    @GET
    @Path("voter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response getByUUID(@PathParam("id") UUID uuid) {
        TypedQuery<Voter> query = em.createQuery("select v FROM Voter v where generatedId = ?1", Voter.class)
                .setParameter(1, uuid);
        try {
            Voter voter = query.getSingleResult();
            Election election = Election.findById(voter.getElection().id);
            Blockchain blockchain = new Blockchain(election.getBlockchainFileName());
            VoterDTO voterDTO = new VoterDTO(voter.getGeneratedId(), voter.getElection().id, voterRepository.hasAlreadyVoted(blockchain, voter));
            return Response.status(Response.Status.OK).entity(voterDTO).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/vote/{electionId}/{candidateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response vote(
            @PathParam("electionId") Long electionId,
            @PathParam("candidateId") Long candidateId,
            Map<String, UUID> requestBody
    ) {
        UUID voterId = requestBody.get("voterId");
        Election election = Election.findById(electionId);
        Candidate candidate = Candidate.findById(candidateId);
        boolean voteIsValid;
        try {
            Voter voter = Voter.findById(voterId);
            voteIsValid = voterRepository.voteForCandidate(voter, candidate, election);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        if (!voteIsValid) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.accepted().build();
    }

    @POST
    @Path("/election/{electionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response createVoters(
            @PathParam("electionId") Long electionId,
            Map<String, Integer> requestBody
    ) {
        Election election = Election.findById(electionId);
        Integer voterCount = requestBody.get("voterCount");

        List<Voter> voters = voterRepository.createVotersForElection(voterCount, election);

        if (voters.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(voters).build();
    }
}
