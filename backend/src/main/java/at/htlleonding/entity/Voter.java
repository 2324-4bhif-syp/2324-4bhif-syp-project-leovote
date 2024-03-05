package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
public class Voter extends PanacheEntityBase {
    //<editor-fold desc="Fields">
    @Id
    @Column(unique = true)
    private UUID generatedId;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Election election;
    private String emailHash;
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

    public Voter(Election election, String emailHash) {
        this.election = election;
        this.emailHash = emailHash;
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

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }
    //</editor-fold>
}
