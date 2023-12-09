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

    @Transactional
    public void initData(@Observes StartupEvent event) {
        // Create and persist candidates
        Candidate candidate1 = new Candidate("id123", "John", "Doe", "12A");
        Candidate candidate2 = new Candidate("id456", "Jane", "Smith", "11B");
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
        entityManager.persist(election1);
        entityManager.persist(election2);

        List<Election> electionList = new ArrayList<>();
        electionList.add(election1);

        List<Election> electionList1 = new ArrayList<>();
        electionList1.add(election2);

        Voter voter = new Voter();
        voter.addParticipating(election1);
        voterRepository.createVotersForElection(10, electionList1);
        voterRepository.createVotersForElection(10, electionList);

        voterRepository.voteForCandidate(voter, candidate1, election1);

        Blockchain blockchain = new Blockchain("Student_Council_Election-3sec-9-12-2023-3sec-16-12-2023.json");
        blockchain.addBlock(candidate1);
        blockchain.addBlock(candidate1);
        blockchain.addBlock(candidate2);
        blockchain.addBlock(candidate2);
    }
}
