package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Election extends PanacheEntity {
    //<editor-fold desc="Fields">
    private String name;
    private LocalDateTime electionStart;
    private LocalDateTime electionEnd;
    private ElectionType electionType;
    private String blockchainFileName;
    private int maxPoints;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    private List<Candidate> participatingCandidates;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Election() {

    }

    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd,
                    ElectionType electionType, List<Candidate> participatingCandidates, int maxPoints) {
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
        this.participatingCandidates = participatingCandidates;
        this.blockchainFileName = String.format("%s-%s%s-%s-%s-%s-%s%s-%s-%s-%s.json", this.name.replaceAll(" ", "_"),
                this.electionStart.getSecond(), "sec", this.electionStart.getDayOfMonth(), this.electionStart.getMonthValue(), this.electionStart.getYear(),
                this.electionEnd.getSecond(), "sec", this.electionEnd.getDayOfMonth(), this.electionEnd.getMonthValue(), this.electionEnd.getYear());
        this.maxPoints = maxPoints;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getBlockchainFileName() {
        return blockchainFileName;
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

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    public List<Candidate> getParticipatingCandidates() {
        return participatingCandidates;
    }

    public void setParticipatingCandidates(List<Candidate> participatingCandidates) {
        this.participatingCandidates = participatingCandidates;
    }

    public int getMaxPoints(){
        return this.maxPoints;
    }

    public void setMaxPoints(int maxPoints){
        this.maxPoints = maxPoints;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    @Override
    public String toString() {
        return "Election{" +
                "name='" + name + '\'' +
                ", electionStart=" + electionStart +
                ", electionEnd=" + electionEnd +
                ", electionType='" + electionType.toString() + '\'' +
                '}';
    }
    //</editor-fold>
}
