package at.htlleonding.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

import java.io.Serializable;

@Embeddable
public class HasVotedId implements Serializable {
    //<editor-fold desc="Fields">
    private Election election;
    private Voter voter;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HasVotedId() {

    }

    public HasVotedId(Election election, Voter voter) {
        this.election = election;
        this.voter = voter;
    }
    //</editor-fold>
}
