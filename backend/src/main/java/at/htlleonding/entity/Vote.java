package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Vote extends PanacheEntity {
    //<editor-fold desc="Fields">
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Candidate candidate;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Vote() {

    }

    public Vote(Candidate candidate, Election election) {
        this.candidate = candidate;
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
