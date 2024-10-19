package at.htlleonding.control;

import at.htlleonding.entity.*;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class VoterRepository implements PanacheRepository<Voter> {
    EntityManager entityManager = Panache.getEntityManager();
    @Inject
    HashService hashService;

    // Creates voters, which are able to vote in elections contained in the election List
    public List<Voter> createVotersForElection(int count, Election election) {
        List<Voter> votersList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Voter voterToAdd = new Voter(election);
            votersList.add(voterToAdd);
            Voter.persist(voterToAdd);
        }
        return votersList;
    }

    // Logic for the vote.
    // Vote passes, if candidate exists, voter hasn't voted, voter is able to vote in election
    // and election exists
    public boolean voteForCandidate(Voter voter, HashMap<Candidate, Integer> voted, Election election) {
        Election election1 = Election.findById(election.id);
        boolean candidatesExist = true;
        for (Map.Entry<Candidate, Integer> entry : voted.entrySet()) {
            Optional<Candidate> candidate1 = election1.getParticipatingCandidates()
                    .stream()
                    .filter(candidate2 -> candidate2 == entry.getKey())
                    .findFirst();
            if(candidate1.isEmpty()) {
                candidatesExist = false;
            }
        }

        Blockchain blockchain = new Blockchain(election1.getBlockchainFileName());

        boolean voteIsValid = candidatesExist &&
                voter.getElection() == election1 &&
                election1.getElectionStart().isBefore(LocalDateTime.now()) &&
                election1.getElectionEnd().isAfter(LocalDateTime.now()) &&
                !hasAlreadyVoted(blockchain, voter);

        if (voteIsValid) {
            entityManager.merge(voter); // If this line is removed, the validity check of the blockhain will fail
            blockchain.addBlock(voted, voter);
            return true;
        }
        return false;
    }


    public boolean hasAlreadyVoted(Blockchain blockchain, Voter voter) {
        for (Block block : blockchain.getBlocks()) {
            if (block.getVoterUUID().equals(voter.getGeneratedId())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEmailAndCode(String email, UUID uuid) {
        TypedQuery<Voter> query = entityManager.createQuery("select v FROM Voter v where generatedId = ?1", Voter.class)
                .setParameter(1, uuid);
        if (query.getSingleResult().getEmailHash() == null) {
            return true;
        }
        String expectedEmailHash = query.getSingleResult().getEmailHash();
        String actualEmailHash = hashService.calculateSHA256Hash(email);
        UUID expectedCode = query.getSingleResult().getGeneratedId();
        Log.info("Actual: " + actualEmailHash);
        Log.info("Expected: " + expectedEmailHash);
        return Objects.equals(actualEmailHash, expectedEmailHash) && Objects.equals(expectedCode, uuid);
    }

    public List<Voter> getVoteCodesByElection(Long electionId) {
        return list("election.id", electionId);
    }
}
