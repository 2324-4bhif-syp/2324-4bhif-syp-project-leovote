package at.htlleonding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vote {
    //<editor-fold desc="Fields">
    @Id
    private Long id;
    @Column(unique = true)
    private String schoolId;
    private String electionId;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote() {

    }
    public Vote(String schoolId, String electionId) {
        this.electionId = electionId;
        this.schoolId = schoolId;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String candidateId) {
        this.schoolId = candidateId;
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
                "candidateId=" + schoolId +
                ", electionId=" + electionId +
                '}';
    }
    //</editor-fold>
}
