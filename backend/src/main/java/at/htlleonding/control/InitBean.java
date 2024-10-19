package at.htlleonding.control;

import at.htlleonding.entity.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class InitBean {
    @Inject
    EntityManager entityManager;
    @Inject
    VoterRepository voterRepository;
    @Inject
    ElectionRepository electionRepository;

    @Transactional
    public void initData(@Observes StartupEvent event) {
        // Create and persist candidates
        Candidate candidate1 = new Candidate("id123", "John", "Doe", "12A", "id123.jpg");
        Candidate candidate2 = new Candidate("id456", "Jane", "Smith", "11B", "id456.jpg");
        entityManager.persist(candidate1);
        entityManager.persist(candidate2);


        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        // Create and persist elections
        Election election1 = new Election(
                "Student Council Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                ElectionType.MULTIVALUE,
                candidateList
        );
        Election election2 = new Election(
                "Class Representative Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                ElectionType.MULTIVALUE,
                candidateList
        );
        entityManager.persist(election1);
        entityManager.persist(election2);

        List<Voter> voters1 = voterRepository.createVotersForElection(10, election1);
        List<Voter> voters2 = voterRepository.createVotersForElection(10, election2);

        for (Voter voter : voters1) {
            System.out.println(voter.getGeneratedId());
            HashMap<Candidate, Integer> vote1 = new HashMap<>();
            vote1.put(candidate1, 6);
            voterRepository.voteForCandidate(voter, vote1, election1);
        }
        try {
            System.out.println((electionRepository.reviewResults(election1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < voters2.size(); i++) {
            Voter voter = voters2.get(i);
            System.out.println(voter.getGeneratedId());
            if (i % 2 == 0) {
                HashMap<Candidate, Integer> vote2 = new HashMap<>();
                vote2.put(candidate2, 6);
                vote2.put(candidate1, 5);
                voterRepository.voteForCandidate(voter, vote2, election2);
            } else {
                HashMap<Candidate, Integer> vote2 = new HashMap<>();
                vote2.put(candidate1, 6);
                voterRepository.voteForCandidate(voter, vote2, election2);
            }
        }
        try {
            System.out.println((electionRepository.reviewResults(election2)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
