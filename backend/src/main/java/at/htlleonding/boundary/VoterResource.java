package at.htlleonding.boundary;

import at.htlleonding.control.Blockchain;
import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import at.htlleonding.entity.dto.CandidateVoteDTO;
import at.htlleonding.entity.dto.VoteRequestDTO;
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

import java.util.HashMap;
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
            VoterDTO voterDTO = new VoterDTO(
                    voter.getGeneratedId(),
                    voter.getElection().id,
                    voterRepository.hasAlreadyVoted(blockchain, voter)
            );

            return Response.status(Response.Status.OK).entity(voterDTO).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    TODO: Remove comment when implemented in frontend
//    Example request body:
//    {
//        "voterId": "fbc07018-49b6-4c4c-9f2f-5ceccf7b11f1",
//            "candidateVotes": [
//        {
//            "candidateId": 1,
//                "points": 5
//        },
//        {
//            "candidateId": 2,
//                "points": 3
//        }
//        ]
//    }

    @POST
    @Path("vote/{electionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response vote(
            @PathParam("electionId") Long electionId,
            VoteRequestDTO voteRequest
    ) {
        UUID voterId = UUID.fromString(voteRequest.getVoterId());
        List<CandidateVoteDTO> candidateVotes = voteRequest.getCandidateVotes();

        Election election = Election.findById(electionId);
        Voter voter;
        HashMap<Candidate, Integer> votesMap = new HashMap<>();

        try {
            voter = Voter.findById(voterId);

            for (CandidateVoteDTO vote : candidateVotes) {
                Long candidateId = vote.getCandidateId();
                int points = vote.getPoints();

                Candidate candidate = Candidate.findById(candidateId);
                votesMap.put(candidate, points);
            }

            boolean voteIsValid = voterRepository.voteForCandidate(voter, votesMap, election);

            if (!voteIsValid) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        return Response.accepted().build();
    }

    @POST
    @Path("election/{electionId}")
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

    @GET
    @Path("voter/{email}/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response checkEmailAndCode(
            @PathParam("email") String email,
            @PathParam("code") UUID uuid
    ) {
        if (voterRepository.checkEmailAndCode(email, uuid)) {
            return Response.accepted().entity(true).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(false).build();

    }
}
