package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"election_id", "voter_id"}))
public class HasVoted extends PanacheEntityBase {
    //<editor-fold desc="Fields">
    @Id
    @GeneratedValue
    private Long id;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
