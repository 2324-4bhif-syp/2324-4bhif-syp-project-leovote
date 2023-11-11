package at.htlleonding.entity;

public class Vote {
    //<editor-fold desc="Fields">
    private Candidate candidate;
    private Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote(Voter voter, Candidate candidate, Election election) {
        this.election = election;
        this.candidate = candidate;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

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
