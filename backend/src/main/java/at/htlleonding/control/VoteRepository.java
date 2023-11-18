package at.htlleonding.control;

import at.htlleonding.entity.Vote;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VoteRepository {
    @Inject
    EntityManager em;

    public VoteRepository() {

    }

    public List<Vote> getAllVotes() {
        return null;
    }

    public Vote getVoteById(Long id){
        return null;
    }

    public void addVote(Vote vote) {
        // Work with Blockchain
    }
}
