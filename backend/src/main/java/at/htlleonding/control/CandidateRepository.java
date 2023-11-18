package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CandidateRepository {
    private final List<Candidate> candidates = new ArrayList<>();

    public CandidateRepository(){

    }

    public List<Candidate> getAllCandidates() {
        return candidates;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        String inputPath = "src/main/resources/candidates.csv";
        Path inputCSVPath = Paths.get(inputPath);
        StringBuilder candidateData = new StringBuilder();
        Log.info(candidateData.toString());

        for (Candidate can : candidates) {
            candidateData.append(can.toCSVString());
        }
        try {
            Files.write(inputCSVPath, candidateData.toString().getBytes());
            System.out.println("Candidates added to the CSV file.");
        } catch (IOException e) {
            Log.error(e);
        }
    }

    public Candidate getCandidateById(Long id){
        return null;
    }

    public void deleteCandidateById(Long id){

    }

    public Optional<Candidate> updateCandidate(Long id, Candidate updatedCandidate){
        return null;
    }
}
