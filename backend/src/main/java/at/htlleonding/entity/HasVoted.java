package at.htlleonding.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
public class HasVoted implements Serializable {
    //<editor-fold desc="Fields">
    @EmbeddedId
    private HasVotedId hasVotedId;
    private boolean voted;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HasVoted() {

    }

    public HasVoted(Election election, Voter voter, boolean voted) {
        this.hasVotedId = new HasVotedId(election, voter);
        this.voted = voted;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public HasVotedId getHasVotedId() {
        return hasVotedId;
    }

    public void setHasVotedId(HasVotedId hasVotedId) {
        this.hasVotedId = hasVotedId;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public Election getElection() {
        return hasVotedId.getElection();
    }

    public void setElection(Election election) {
        this.hasVotedId.setElection(election);
    }

    public Voter getVoter() {
        return hasVotedId.getVoter();
    }

    public void setVoter(Voter voter) {
        this.hasVotedId.setVoter(voter);
    }
    //</editor-fold>
}
