package at.htlleonding.entity;

import jakarta.persistence.*;

@Entity
public class Vote {
    //<editor-fold desc="Fields">
    @Id
    private Long id;
    @ManyToOne
    private Candidate candidate;
    @ManyToOne
    private Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote() {

    }

    public Vote(Candidate candidate, Election election) {
        this.candidate = candidate;
        this.election = election;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    //<editor-fold desc="Methods">
    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", candidate=" + candidate +
                ", election=" + election +
                '}';
    }
    //</editor-fold>
}
