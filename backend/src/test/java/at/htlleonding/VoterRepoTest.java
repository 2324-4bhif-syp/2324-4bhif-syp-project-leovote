package at.htlleonding;

import at.htlleonding.control.ElectionRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class VoterRepoTest {
    @Inject
    EntityManager entityManager;

    @Inject
    ElectionRepository electionRepository;

    @Inject
    VoterRepository voterRepository;

    @Test
    @Transactional
    public void Election_Test_good() {
        //arrange
        Candidate candidate_winner = new Candidate("id1", "c1", "winner", "1a");
        Candidate candidate_loser = new Candidate("id2", "c2", "looser", "1a");
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate_winner);
        entityManager.persist(candidate_loser);
        candidateList.add(candidate_winner);
        candidateList.add(candidate_loser);
        Election election1 = new Election(
                "TestElection1",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        List<Voter> voterList_winner = voterRepository.createVotersForElection(8, election1);
        for (Voter v : voterList_winner) {
            voterRepository.voteForCandidate(v, candidate_winner, election1);
        }
        List<Voter> voterList_loser = voterRepository.createVotersForElection(2, election1);
        for (Voter v : voterList_loser) {
            voterRepository.voteForCandidate(v, candidate_loser, election1);
        }

        //assert
        assertThat(candidateList.size()).isEqualTo(2);
        System.out.println("CandidateList Size good \n\n");
        for (int i = 0; i < candidateList.size(); i++) {
            assertThat(election1.getParticipatingCandidates().get(i)).isEqualTo(candidateList.get(i));
        }
        System.out.println("Candidates are equal to electionCandidates \n\n");

        try {
            assertThat(electionRepository.reviewResults(election1).get(candidate_winner)).isEqualTo(80);
            assertThat(electionRepository.reviewResults(election1).get(candidate_loser)).isEqualTo(20);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Candidates results are good");

        for (Voter v : voterList_winner) {
            assertThat(v.isVoted()).isEqualTo(true);
        }
        for (Voter v : voterList_loser) {
            assertThat(v.isVoted()).isEqualTo(true);
        }

        //after
        entityManager.remove(candidate_winner);
        entityManager.remove(candidate_loser);
        for (Voter v : voterList_winner) {
            entityManager.remove(v);
        }
        for (Voter v : voterList_loser) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }

    @Test
    @Transactional
    public void Election_Test_Candidate_not_in_Election() {
        //arrange
        Candidate candidate_notInElection = new Candidate("id2", "c2", "notInElection", "2b");
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate_notInElection);
        Election election1 = new Election(
                "TestElection2",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        List<Voter> voterList = voterRepository.createVotersForElection(10, election1);
        for (Voter v : voterList) {
            voterRepository.voteForCandidate(v, candidate_notInElection, election1);
        }

        //assert
        for (int i = 0; i < candidateList.size(); i++) {
            assertThat(election1.getParticipatingCandidates().get(i)).isEqualTo(candidateList.get(i));
            System.out.println("Candidate " + i + " equal to electionCandidate :) \n\n");
        }

        try {
            assertThat(electionRepository.reviewResults(election1).get(candidate_notInElection)).isEqualTo(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Voter v : voterList) {
            assertThat(v.isVoted()).isEqualTo(false);
        }

        //after
        entityManager.remove(candidate_notInElection);
        for (Voter v : voterList) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }

    @Test
    @Transactional
    public void Election_Test_Voter_in_no_Election() {
        //arrange
        Candidate candidate1 = new Candidate("id1", "c1", "notInElection", "1b");
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate1);
        Election election1 = new Election(
                "TestElection2",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        List<Voter> voterList = voterRepository.createVotersForElection(10, election1);
        for (Voter v : voterList) {
            voterRepository.voteForCandidate(v, candidate1, election1);

        }

        //assert
        assertThat(candidate1).isNotIn(election1.getParticipatingCandidates());
        System.out.println("Candidate1 not in Election :) \n\n");

        try {
            assertThat(electionRepository.reviewResults(election1).get(candidate1)).isEqualTo(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Voter v : voterList) {
            assertThat(v.isVoted()).isEqualTo(false);
        }

        //after0
        entityManager.remove(candidate1);
        for (Voter v : voterList) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }
}