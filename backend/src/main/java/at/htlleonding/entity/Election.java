package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Election extends PanacheEntity {
    //<editor-fold desc="Fields">
    private String name;
    private LocalDateTime electionStart;
    private LocalDateTime electionEnd;
    private String electionType;
    private Blockchain blockchain;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    private List<Candidate> participatingCandidates;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Election() {

    }

    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd, String electionType, List<Candidate> participatingCandidates) {
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
        this.participatingCandidates = participatingCandidates;
        this.blockchain = new Blockchain(this);
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public Blockchain getBlockchain(){
        return this.blockchain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getElectionStart() {
        return electionStart;
    }

    public void setElectionStart(LocalDateTime electionStart) {
        this.electionStart = electionStart;
    }

    public LocalDateTime getElectionEnd() {
        return electionEnd;
    }

    public void setElectionEnd(LocalDateTime electionEnd) {
        this.electionEnd = electionEnd;
    }

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    public List<Candidate> getParticipatingCandidates() {
        return participatingCandidates;
    }

    public void setParticipatingCandidates(List<Candidate> participatingCandidates) {
        this.participatingCandidates = participatingCandidates;
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
