package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
public class Voter extends PanacheEntityBase {
    //<editor-fold desc="Fields">
    @Id
    @Column(unique = true)
    private String generatedId;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {
        election = null;
        this.generatedId = UUID.randomUUID().toString();
    }

    public Voter(Election election) {
        this.election = election;
        this.generatedId = UUID.randomUUID().toString();
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getGeneratedId() {
        return generatedId;
    }

    public void appendToCode(String part) {
        this.generatedId += part;
    }

    public Election getElection() {
        return election;
    }

    //</editor-fold>
}
