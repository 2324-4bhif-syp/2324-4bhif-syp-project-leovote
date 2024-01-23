package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Voter extends PanacheEntityBase {
    //<editor-fold desc="Fields">
    @Id
    @Column(unique = true)
    private UUID generatedId;
    @ManyToOne
    private Election election;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {
        election = null;
        this.generatedId = UUID.randomUUID();
    }

    public Voter(Election election) {
        this.election = election;
        this.generatedId = UUID.randomUUID();
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public UUID getGeneratedId() {
        return generatedId;
    }

    public Election getElection() {
        return election;
    }

    //</editor-fold>
}
