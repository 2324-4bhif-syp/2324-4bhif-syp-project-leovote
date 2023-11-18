package at.htlleonding.control;

import at.htlleonding.entity.Voter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class VoterRepository {
    @Inject
    EntityManager em;

    public VoterRepository() {

    }

    public List<Voter> getAllVoters() {
        return null;
    }

    public Voter getVoterById(Long id){
        return null;
    }

    public void addVoter(Voter voter) {

    }

    public void deleteVoterById(Long id){

    }

    public Optional<Voter> updateVoterById(Long id, Voter updatedVoter){
        return null;
    }
}
