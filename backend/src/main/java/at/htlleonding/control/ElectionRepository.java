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

    public HashMap<Candidate, Double> reviewResults (Election election){
        HashMap<Candidate, Integer> voteCounts = new HashMap<>();
        Blockchain blockchain = new Blockchain(election.getBlockchainFileName());
        List<Block> chain = blockchain.getBlocks();

        // Add candidates to Hashmap
        for(Candidate candidate: election.getParticipatingCandidates()){
            voteCounts.put(candidate, 0);
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
}
