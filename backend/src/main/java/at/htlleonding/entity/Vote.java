package at.htlleonding.entity;

public class Vote {
    //<editor-fold desc="Fields">
    private Voter voter;
    private Candidate candidate;
    private Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote(Voter voter, Candidate candidate, Election election) {
        this.voter = voter;
        this.election = election;
        this.candidate = candidate;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
    //</editor-fold>
}
