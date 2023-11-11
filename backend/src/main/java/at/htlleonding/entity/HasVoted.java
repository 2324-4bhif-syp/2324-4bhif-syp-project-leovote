package at.htlleonding.entity;

public class HasVoted {
    private Election election;
    private Voter voter;
    private boolean voted;

    public HasVoted(Election election, Voter voter, boolean voted) {
        this.election = election;
        this.voter = voter;
        this.voted = voted;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }
}
