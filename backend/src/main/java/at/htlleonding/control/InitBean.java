package at.htlleonding.control;

import at.htlleonding.entity.*;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class InitBean {
    @Inject
    CandidateRepository candidateRepository;
    @Inject
    EntityManager entityManager;

    @Transactional
    public void initData(@Observes StartupEvent event) {
        // Create and persist elections
        Election election1 = new Election(
                "Student Council Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "General");
        Election election2 = new Election(
                "Class Representative Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Class"
        );
        entityManager.persist(election1);
        entityManager.persist(election2);

        // Create and persist candidates
        Candidate candidate1 = new Candidate("id123", "John", "Doe", "12A");
        Candidate candidate2 = new Candidate("id456", "Jane", "Smith", "11B");
        entityManager.persist(candidate1);
        entityManager.persist(candidate2);

        // Create and persist voters
        Voter voter1 = new Voter("student1", "password1");
        Voter voter2 = new Voter("student2", "password2");
        entityManager.persist(voter1);
        entityManager.persist(voter2);

        // Create and persist votes
        Vote vote1 = new Vote(candidate1, election1);
        Vote vote2 = new Vote(candidate2, election1);
        entityManager.persist(vote1);
        entityManager.persist(vote2);

        // Create and persist hasVoted entries
        HasVoted hasVoted1 = new HasVoted(election1, voter1, true);
        HasVoted hasVoted2 = new HasVoted(election1, voter2, true);
        entityManager.persist(hasVoted1);
        entityManager.persist(hasVoted2);
    }

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
                candidateRepository.addCandidate(new Candidate(elements[0], elements[1], elements[2], elements[3]));
            }
        } catch (Exception e) {
            Log.error(String.format("FAILED: %s, %s", inputCSVPath, e.getMessage()));
        }
        Log.info("Setup: completed setup");
    }
}
