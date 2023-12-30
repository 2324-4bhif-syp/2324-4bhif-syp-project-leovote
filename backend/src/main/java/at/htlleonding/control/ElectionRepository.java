package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ElectionRepository implements PanacheRepository<Election> {
    private final HashService hashService = new HashService();

    public HashMap<Candidate, Double> reviewResults(Election election) throws Exception {
        HashMap<Candidate, Integer> voteCounts = new HashMap<>();
        Blockchain blockchain = new Blockchain(election.getBlockchainFileName());
        List<Block> chain = blockchain.getBlocks();

        // Add candidates to Hashmap
        for (Candidate candidate : election.getParticipatingCandidates()) {
            voteCounts.put(candidate, 0);
        }

        String lastHash = "";
        for (Block block : chain) {
            if (!block.getPreviousHash().equals("0") &&
                    !block.getPreviousHash().equals(lastHash)) {
                throw new Exception("Hashes don't fit. Could've been manipulated");
            }
            lastHash = block.getHash();

            if (!block.getHash().equals(calculateHash(block))) {
                throw new Exception("Hashes don't fit. Could've been manipulated");
            }
        }

        // Assuming the Candidate class has proper equals() and hashCode() implementations
        for (int i = 1; i < chain.size(); i++) {
            Candidate votedCandidate = chain.get(i).getVote();
            System.out.println((votedCandidate.getFirstName() + " " + votedCandidate.getSchoolId()));

            // Update vote count for the candidate
            voteCounts.put(votedCandidate, voteCounts.getOrDefault(votedCandidate, 0) + 1);
        }

        // Calculate percentages
        int totalVotes = blockchain.getBlocks().size() - 1; // excluding genesis block
        HashMap<Candidate, Double> results = new HashMap<>();
        for (Candidate candidate : voteCounts.keySet()) {
            int votes = voteCounts.get(candidate);
            double percentage = (double) votes / totalVotes * 100.0;
            results.put(candidate, percentage);
        }
        return results;
    }

    private String calculateHash(Block block) {
        String data = block.getIndex() + block.getTimestamp() + block.getVote().toString() + block.getPreviousHash() + block.getVoterUUID();
        return hashService.calculateSHA256Hash(data);
    }

}
