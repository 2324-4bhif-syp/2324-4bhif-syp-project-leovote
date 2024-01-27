package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Email;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    public List<String> getVotersEmails(Long electionId) {
        TypedQuery<Email> query = Panache.getEntityManager()
                .createQuery("select e from Email e where e.election.id = :electionId", Email.class);
        query.setParameter("electionId", electionId);
        List<Email> emails = query.getResultList();
        return emails.stream().map(Email::getEmail).toList();
    }

    public Optional<Email> addEmailtoElection(Long electionId, String email) {
        Optional<Election> election = Election.findByIdOptional(electionId);
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\" +
                "x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[" +
                "a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(2(5[0-5]|[0-4][0-9])|1[0-" +
                "9][0-9]|[1-9]?[0-9])\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]" +
                ":(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]" +
                ")+)])";

        if (election.isPresent() && email.matches(emailRegex)) {
            Email emailCreated = new Email(email, election.get());
            Email.persist(emailCreated);
            return Optional.of(emailCreated);
        }

        return Optional.empty();
    }

    public void removeEmailFromElection(Long emailId) {
        Email.deleteById(emailId);
    }
}
