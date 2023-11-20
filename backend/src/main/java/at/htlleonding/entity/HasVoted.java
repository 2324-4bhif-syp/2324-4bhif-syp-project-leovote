package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueElectionAndVoter", columnNames = {"election_id", "voter_id"})})
public class HasVoted extends PanacheEntity {
    //<editor-fold desc="Fields">
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Election election;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Voter voter;
    private boolean voted;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HasVoted() {

    }

    public HasVoted(Election election, Voter voter, boolean voted) {
        this.election = election;
        this.voter = voter;
        this.voted = voted;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
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
    //</editor-fold>
}
