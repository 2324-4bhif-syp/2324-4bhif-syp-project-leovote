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
    private Election participatingIn;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {
        participatingIn = null;
        this.generatedId = UUID.randomUUID();
    }

    public Voter(Election participatingIn) {
        this.participatingIn = participatingIn;
        this.generatedId = UUID.randomUUID();
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public UUID getGeneratedId() {
        return generatedId;
    }

    public Election getParticipatingIn() {
        return participatingIn;
    }

    //</editor-fold>
}
