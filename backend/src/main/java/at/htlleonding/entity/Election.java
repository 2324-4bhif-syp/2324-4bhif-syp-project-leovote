package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Election extends PanacheEntity {
    //<editor-fold desc="Fields">
    public String name;
    public LocalDateTime electionStart;
    public LocalDateTime electionEnd;
    public String electionType;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Election() {

    }

    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd, String electionType) {
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    @Override
    public String toString() {
        return "Election{" +
                "name='" + name + '\'' +
                ", electionStart=" + electionStart +
                ", electionEnd=" + electionEnd +
                ", electionType='" + electionType + '\'' +
                '}';
    }
    //</editor-fold>
}
