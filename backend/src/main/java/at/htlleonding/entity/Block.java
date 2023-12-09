package at.htlleonding.entity;

import at.htlleonding.control.HashService;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Block {
    //<editor-fold desc="Fields">
    private final HashService hashService = new HashService();
    private final int index;
    private final Long timestamp;
    private final Candidate voted;
    private final String previousHash;
    private final String hash;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Block(@JsonProperty("index") int index, @JsonProperty("timestamp") Long timestamp, @JsonProperty("vote") Candidate voted, @JsonProperty("previousHash") String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.voted = voted;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    private String calculateHash() {
        String data = index + timestamp + voted.toString() + previousHash;
        return hashService.calculateSHA256Hash(data);
    }

    public String getHash() {
        return hash;
    }

    public Candidate getVote() {
        return voted;
    }

    public int getIndex() {
        return index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }
    //</editor-fold>
}
