package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CandidateRepository {
    private List<Candidate> candidates;

    public CandidateRepository() {
        candidates = new ArrayList<>();
        createExampleData();
    }

    private void createExampleData() {
        candidates.add(new Candidate("if20020", "Max", "Mustermann", "4BHIF"));
        candidates.add(new Candidate("if22320", "David", "Graf", "3AHITM"));
        candidates.add(new Candidate("if26765", "Gustav", "Eder", "5AHIF"));
        candidates.add(new Candidate("if23757", "Ella", "Lang", "3CHIF"));
        candidates.add(new Candidate("if22781", "Lena", "Zauner", "2BHIF"));
    }
    public List<Candidate> getAllCandidates() {
        return candidates;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}
