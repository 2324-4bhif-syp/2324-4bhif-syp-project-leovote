package at.htlleonding.control;

import at.htlleonding.entity.*;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ElectionRepository implements PanacheRepository<Election> {
    @Inject
    HashService hashService;

    @Inject
    ChainHashRepository chainHashReporitory;

    private static final String blockchainFileDir = "src/main/resources/blockchain/";

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

        String returnedHash = hashService.getFileHash(blockchainFileDir + election.getBlockchainFileName());
        Optional<ChainHash> first = chainHashReporitory.find("filePath",
                blockchainFileDir + election.getBlockchainFileName()).firstResultOptional();

        if(first.isPresent()) {
            if (!returnedHash.equals(first.get().getHash())) {
                throw new Exception("Hashes don't fit. Could've been manipulated");
            }
        }
//        } else {
//            throw new Exception("Hashes don't fit. Could've been manipulated");
//        }
        // Assuming the Candidate class has proper equals() and hashCode() implementations
        for (int i = 1; i < chain.size(); i++) {
            HashMap<Candidate, Integer> votedCandidate = chain.get(i).getVoted();

            // Update vote count for the candidate
            for(Map.Entry<Candidate, Integer> entry : votedCandidate.entrySet()) {
                voteCounts.put(entry.getKey(), voteCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        // Calculate percentages
        int totalVotes = 0;
        for(Map.Entry<Candidate, Integer> entry : voteCounts.entrySet()) {
            totalVotes += entry.getValue();
        }

        HashMap<Candidate, Double> results = new HashMap<>();
        for (Candidate candidate : voteCounts.keySet()) {
            int votes = voteCounts.get(candidate);
            double percentage = (double) votes / totalVotes * 100.0;
            results.put(candidate, percentage);
        }
        return results;
    }

    public Election createNewElection(Election election) {
        Election election1 = new Election(election.getName(), election.getElectionStart(), election.getElectionEnd()
                , election.getElectionType(), election.getParticipatingCandidates(), election.getMaxPoints());
        getEntityManager().persist(election1);
        return election1;
    }

    private String calculateHash(Block block) {
        String data = block.toString();
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
                "9][0-9]|[1-9]?[0-9])\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:" +
                "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

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


    public void deleteElection(Election election) {
        Election.delete("id", election.id);

    }
}
