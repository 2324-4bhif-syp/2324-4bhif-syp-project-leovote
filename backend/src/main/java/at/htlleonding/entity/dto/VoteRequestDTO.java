package at.htlleonding.entity.dto;

import java.util.List;

public class VoteRequestDTO {
    private String voterId;
    private List<CandidateVoteDTO> candidateVotes;

    // Getters and setters
    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public List<CandidateVoteDTO> getCandidateVotes() {
        return candidateVotes;
    }

    public void setCandidateVotes(List<CandidateVoteDTO> candidateVotes) {
        this.candidateVotes = candidateVotes;
    }
}
