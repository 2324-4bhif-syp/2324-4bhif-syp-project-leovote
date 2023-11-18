package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class HasVoted extends PanacheEntityBase implements Serializable {
    //<editor-fold desc="Fields">
    @EmbeddedId
    public HasVotedId hasVotedId;
    public boolean voted;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HasVoted() {

    }

    public HasVoted(Election election, Voter voter, boolean voted) {
        this.hasVotedId = new HasVotedId(election, voter);
        this.voted = voted;
    }
    //</editor-fold>
}
