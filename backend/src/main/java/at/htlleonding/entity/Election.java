package at.htlleonding.entity;

import java.time.LocalDateTime;

public class Election {
    private String name;
    private LocalDateTime electionStart;
    private LocalDateTime electionEnd;
    private String electionType;

    public Election(String name, LocalDateTime electionStart, LocalDateTime electionEnd, String electionType){
        this.name = name;
        this.electionStart = electionStart;
        this.electionEnd = electionEnd;
        this.electionType = electionType;
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
}
