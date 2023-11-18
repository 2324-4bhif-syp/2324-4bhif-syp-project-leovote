package at.htlleonding.control;

import at.htlleonding.entity.Election;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ElectionRepository {

    @Inject
    EntityManager em;

    public ElectionRepository() {

    }

    public List<Election> getAllElections() {
        return null;
    }

    public Election getElectionById(Long id){
        return null;
    }

    public void addElection(Election election) {

    }

    public void deleteElectionById(Long id){

    }

    public Optional<Election> updateElectionById(Long id, Election updatedElection){
        return null;
    }

}
