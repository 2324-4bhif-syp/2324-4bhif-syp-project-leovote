package at.htlleonding.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;

@Entity
public class HasVoted {
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
    //</editor-fold>
}
