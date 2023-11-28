package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
public class Voter extends PanacheEntity {
    //<editor-fold desc="Fields">
    @Column(unique = true)
    private UUID generatedId;
    @OneToMany
    private List<Election> participatingIn;
    private boolean voted;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {
        participatingIn = new ArrayList<>();
        this.generatedId = UUID.randomUUID();
        this.voted = false;
    }

    public Voter(List<Election> participatingIn) {
        this.participatingIn = participatingIn;
        this.generatedId = UUID.randomUUID();
        this.voted = false;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public UUID getGeneratedId() {
        return generatedId;
    }

    public void addParticipating(Election election){
        if(!participatingIn.contains(election)){
            participatingIn.add(election);
        }
    }

    public List<Election> getParticipatingIn() {
        return participatingIn;
    }

    public void setParticipatingIn(List<Election> participatingIn) {
        this.participatingIn = participatingIn;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    //</editor-fold>
}
