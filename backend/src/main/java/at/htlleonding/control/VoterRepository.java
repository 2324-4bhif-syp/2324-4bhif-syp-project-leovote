package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class VoterRepository implements PanacheRepository<Voter> {
    @Inject
    EntityManager entityManager;

    // Creates voters, which are able to vote in elections contained in the election List
    public List<Voter> createVotersForElection(int count, List<Election> election){
        List<Voter> votersList = new ArrayList<>();
        for(int i = 0; i < count; i++){
            Voter voterToAdd = new Voter(election);
            votersList.add(voterToAdd);
            entityManager.persist(voterToAdd);
        }
        return votersList;
    }

    // Logic for the vote.
    // Vote passes, if candidate exists, voter hasn't voted, voter is able to vote in election
    // and election exists
    public boolean voteForCandidate(Voter voter, Candidate candidate, Election election){
        Election election1 = entityManager.find(Election.class, election.id);
        Optional<Candidate> candidate1 = election1.getParticipatingCandidates()
                .stream()
                .filter(candidate2 -> candidate2 == candidate)
                .findFirst();
        if(candidate1.isPresent() && election1 != null && !voter.isVoted()
                && voter.getParticipatingIn().contains(election)){
            voter.setVoted(true);
            entityManager.merge(voter);
            return true;
            // Blockchain logic has to be added
        }
        return false;
    }
}
