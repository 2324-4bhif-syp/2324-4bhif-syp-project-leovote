package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Embeddable
public class HasVotedId extends PanacheEntityBase implements Serializable {
    //<editor-fold desc="Fields">
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Election election;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Voter voter;
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
