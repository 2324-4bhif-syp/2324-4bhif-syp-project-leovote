package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InitBean {
    @Inject
    CandidateRepository candidateRepository;
    @Transactional
    void startUp(@Observes StartupEvent event) {
        Log.info("It is working!");
        String inputPath = "src/main/resources/candidates.csv";
        Path inputCSVPath = Paths.get(inputPath);
        try {
            Log.info(String.format("Started reading from path %s", inputCSVPath));
            List<String> lines = Files.readAllLines(inputCSVPath, StandardCharsets.UTF_8);
            Log.info(String.format("Finish reading from path %s", inputCSVPath));
            Log.info("Parsing: started parsing csv file into candidate objects");
            for (String line : lines) {
                String[] elements = line.split(";");
                candidateRepository.addCandidate(
                        new Candidate(
                                elements[0],
                                elements[1],
                                elements[2],
                                elements[3]
                        )
                );
            }
        } catch (Exception e) {
            Log.error(String.format("FAILED: %s, %s", inputCSVPath, e.getMessage()));
        }
        Log.info("Setup: completed setup");}
}
