package at.htlleonding.control;

import at.htlleonding.entity.Election;
import at.htlleonding.entity.HasVoted;
import at.htlleonding.entity.HasVotedId;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class HasVotedRepository {
    @Inject
    EntityManager em;

    public HasVotedRepository() {

    }

    public List<HasVoted> getAllHasVoted() {
        return null;
    }

    public HasVoted getHasVotedById(HasVotedId id){
        return null;
    }

    public void addHasVoted(HasVoted hasVoted) {

    }

    public void deleteHasVotedById(HasVotedId id){

    }

    public Optional<Election> updateHasVoted(HasVotedId id, HasVoted updatedHasVoted){
        return null;
    }
}
