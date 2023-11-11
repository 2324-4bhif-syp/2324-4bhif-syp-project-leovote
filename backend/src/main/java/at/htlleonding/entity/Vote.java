package at.htlleonding.entity;

public class Vote {
    //<editor-fold desc="Fields">
    private String candidateId;
    private String electionId;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote(String candidateId, String electionId) {
        this.electionId = electionId;
        this.candidateId = candidateId;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }
    //</editor-fold>

    //<editor-fold desc="Vote">
    @Override
    public String toString() {
        return "Vote{" +
                "candidateId=" + candidateId +
                ", electionId=" + electionId +
                '}';
    }
    //</editor-fold>
}
