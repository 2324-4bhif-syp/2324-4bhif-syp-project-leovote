package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Election extends PanacheEntity {
    //<editor-fold desc="Fields">
    public String name;
    public LocalDateTime electionStart;
    public LocalDateTime electionEnd;
    public String electionType;
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    public List<Candidate> voteables;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Election() {

    }

    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd, String electionType, List<Candidate> voteables) {
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
        this.voteables = voteables;
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
