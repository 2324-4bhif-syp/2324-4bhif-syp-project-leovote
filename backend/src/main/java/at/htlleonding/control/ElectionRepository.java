package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.HashMap;

@ApplicationScoped
public class ElectionRepository implements PanacheRepository<Election> {
    EntityManager entityManager = Panache.getEntityManager();

    public HashMap<Candidate, Double> reviewResults (Election election){
        HashMap<Candidate, Integer> voteCounts = new HashMap<>();
        Blockchain blockchain = new Blockchain(election.getBlockchainFileName());

        // Assuming the Candidate class has proper equals() and hashCode() implementations
        for (Block block : blockchain.getBlocks()) {
            Candidate votedCandidate = block.getVote();

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
}
