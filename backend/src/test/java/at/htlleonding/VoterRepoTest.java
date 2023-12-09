package at.htlleonding;

import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class VoterRepoTest {
    @Inject
    EntityManager entityManager;

    @Inject
    VoterRepository voterRepository;

    @Test
    @Transactional
    public void Election_good() {
        //arrange
        Candidate candidate1 = new Candidate("id1", "c1", "votedFor", "1a");
        Candidate candidate2 = new Candidate("id2", "c2", "notVotedFor", "2b");
        List<Election> electionList = new ArrayList<>();
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate1);
        entityManager.persist(candidate2);
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        Election election1 = new Election(
                "TestElection1",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        electionList.add(election1);
        List<Voter> voterList = voterRepository.createVotersForElection(10, electionList);
        for (Voter v : voterList) {
            voterRepository.voteForCandidate(v, candidate1, election1);
        }

        //assert
        for (int i = 0; i < candidateList.size(); i++) {
            assertThat(election1.getParticipatingCandidates().get(i)).isEqualTo(candidateList.get(i));
            System.out.println("Candidate "+ i + " equal to electionCandidate :) \n\n");
        }

    }
}