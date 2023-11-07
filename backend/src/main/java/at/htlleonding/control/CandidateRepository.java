package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CandidateRepository {
    private List<Candidate> candidates = new ArrayList<>();
    public List<Candidate> getAllCandidates() {
        return candidates;
    }
    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}
