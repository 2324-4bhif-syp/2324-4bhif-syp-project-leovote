package at.htlleonding.boundary;

import at.htlleonding.control.AuthorizationService;
import at.htlleonding.control.Blockchain;
import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import at.htlleonding.entity.dto.CandidateVoteDTO;
import at.htlleonding.entity.dto.VoteRequestDTO;
import at.htlleonding.entity.dto.VoterDTO;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.*;

import static at.htlleonding.boundary.Roles.ADMIN_ROLE;
import static at.htlleonding.boundary.Roles.USER_ROLE;

@Path("voters")
public class VoterResource {
    @Inject
    VoterRepository voterRepository;
    @Inject
    EntityManager em;
    @Inject
    AuthorizationService authorizationService;
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("voter/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getByUUID(@PathParam("id") UUID uuid) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE) && !authorizationService.hasAccess(jwt, USER_ROLE)) {
            return Response.status(403).build();
        }

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

    @POST
    @Path("vote/{electionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response vote(
            @PathParam("electionId") Long electionId,
            VoteRequestDTO voteRequest
    ) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE) && !authorizationService.hasAccess(jwt, USER_ROLE)) {
            return Response.status(403).build();
        }

        UUID voterId = UUID.fromString(voteRequest.getVoterId());
        List<CandidateVoteDTO> candidateVotes = voteRequest.getCandidateVotes();

        Election election = Election.findById(electionId);
        Voter voter;
        HashMap<Candidate, Integer> votesMap = new HashMap<>();

        if (!checkPoints(voteRequest, election)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

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
    public Response createVoters(
            @PathParam("electionId") Long electionId,
            Map<String, Integer> requestBody
    ) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

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
    public Response checkEmailAndCode(
            @PathParam("email") String email,
            @PathParam("code") UUID uuid
    ) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE) && !authorizationService.hasAccess(jwt, USER_ROLE)) {
            return Response.status(403).build();
        }

        if (voterRepository.checkEmailAndCode(email, uuid)) {
            return Response.accepted().entity(true).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(false).build();

    }

    private boolean checkPoints(VoteRequestDTO voteRequest, Election election) {
        // Set, to check if Points only exist one time.
        Set<Integer> uniquePoints = new HashSet<>();

        // check for max value
        int maxPoints = election.getMaxPoints();

        for (CandidateVoteDTO voteDTO : voteRequest.getCandidateVotes()) {
            // Check if points are between 0 and max value
            int points = voteDTO.getPoints();
            if (points < 0 || points > maxPoints) {
                return false;
            }

            // Check for duplicated points
            if (!uniquePoints.add(points)) {
                return false;
            }
        }

        return true;
    }
}
