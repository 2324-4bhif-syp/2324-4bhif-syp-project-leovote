package at.htlleonding.entity;

import at.htlleonding.control.HashService;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

public class Election {
    //<editor-fold desc="Fields">
    @Inject
    HashService hashService;
    private String id;
    private String name;
    private LocalDateTime electionStart;
    private LocalDateTime electionEnd;
    private String electionType;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd, String electionType) {
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
        this.id = hashService.calculateSHA256Hash(this.toString());
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = hashService.calculateSHA256Hash(this.toString());
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
