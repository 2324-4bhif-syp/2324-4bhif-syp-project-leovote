package at.htlleonding.control;

import at.htlleonding.entity.Vote;

import java.util.ArrayList;
import java.util.List;

public class VoteRepository {
    private final List<Vote> votes = new ArrayList<>();

    public List<Vote> getAllVotes() {
        return votes;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }
}
