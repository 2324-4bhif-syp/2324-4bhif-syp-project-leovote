package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String inputPath = "src/main/resources/candidates.csv";
        Path inputCSVPath = Paths.get(inputPath);
        String candidateData = "";
        Log.info(candidateData);

        for (Candidate can : candidates) {
            candidateData += can.toCSVString();
        }
        try {
            Files.write(inputCSVPath, candidateData.getBytes());
            System.out.println("Candidates added to the CSV file.");
        } catch (IOException e) {
            Log.error(e);
        }
    }
}
