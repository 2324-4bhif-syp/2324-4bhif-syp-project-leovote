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
        Candidate candidate1 = new Candidate("id123", "John", "Doe", "12A", "john-doe.jpg");
        Candidate candidate2 = new Candidate("id456", "Jane", "Smith", "11B", "mary-doe.jpg");
        //For large Vote
        Candidate candidate3 = new Candidate("id13", "John", "Doe", "12A", "john-doe.jpg");
        Candidate candidate4 = new Candidate("id14", "Jane", "Smith1", "11B", "mary-doe.jpg");
        Candidate candidate5 = new Candidate("id415", "Jane", "Smith2", "11B", "mary-doe.jpg");
        Candidate candidate6 = new Candidate("id416", "Jane", "Smith3", "11B", "mary-doe.jpg");
        Candidate candidate7 = new Candidate("id417", "Jane", "Smith4", "11B", "mary-doe.jpg");
        Candidate candidate8 = new Candidate("id418", "Jane", "Smith5", "11B", "mary-doe.jpg");

        entityManager.persist(candidate1);
        entityManager.persist(candidate2);
        entityManager.persist(candidate3);
        entityManager.persist(candidate4);
        entityManager.persist(candidate5);
        entityManager.persist(candidate6);
        entityManager.persist(candidate7);
        entityManager.persist(candidate8);

        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        // Create and persist elections
        Election election1 = new Election(
                "Student Council Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "General",
                candidateList
        );
        Election election2 = new Election(
                "Class Representative Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Class",
                candidateList
        );
        candidateList.add(candidate3);
        candidateList.add(candidate4);
        candidateList.add(candidate5);
        candidateList.add(candidate6);
        candidateList.add(candidate7);
        candidateList.add(candidate8);

        Election election3 = new Election(
                "LargeVote Election",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Class",
                candidateList
        );
        entityManager.persist(election1);
        entityManager.persist(election2);
        entityManager.persist(election3);

        List<Voter> voters1 = voterRepository.createVotersForElection(10, election1);
        List<Voter> voters2 = voterRepository.createVotersForElection(10, election2);
        List<Voter> voters3 = voterRepository.createVotersForElection(10, election3);

        for (Voter voter : voters1) {
            System.out.println(voter.getGeneratedId());
            voterRepository.voteForCandidate(voter, candidate1, election1);
            voterRepository.voteForCandidate(voter, candidate1, election1);
        }
        for (Voter voter : voters3) {
            voterRepository.voteForCandidate(voter, candidate7, election3);
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
                voterRepository.voteForCandidate(voter, candidate2, election2);
            } else {
                voterRepository.voteForCandidate(voter, candidate1, election2);
            }
        }
        try {
            System.out.println((electionRepository.reviewResults(election2)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
