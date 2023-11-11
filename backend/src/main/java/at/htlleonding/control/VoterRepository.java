package at.htlleonding.control;

import at.htlleonding.entity.Vote;
import at.htlleonding.entity.Voter;

import java.util.ArrayList;
import java.util.List;

public class VoterRepository {
    private final List<Voter> voters = new ArrayList<>();

    public List<Voter> getAllVoteres() {
        return voters;
    }

    public void addVoter(Voter voter) {
        voters.add(voter);
    }
}
