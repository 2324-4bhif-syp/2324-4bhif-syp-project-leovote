package at.htlleonding.entity.dto;

public class CandidateVoteDTO {
    private Long candidateId;
    private int points;

    // Getters and setters
    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
