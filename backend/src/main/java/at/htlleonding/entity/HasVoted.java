package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"election_id", "voter_id"}))
public class HasVoted extends PanacheEntity {
    //<editor-fold desc="Fields">
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Election election;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Voter voter;
    public boolean voted;
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
}
