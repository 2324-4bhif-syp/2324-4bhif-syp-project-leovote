package at.htlleonding.entity;

import at.htlleonding.control.HashService;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Block {
    //<editor-fold desc="Fields">
    private final HashService hashService = new HashService();
    private final int index;
    private final Long timestamp;
    private final Candidate voted;
    private final String previousHash;
    private final String hash;
    private final UUID voterUUID;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Block(@JsonProperty("index") int index, @JsonProperty("timestamp") Long timestamp,
                 @JsonProperty("voted") Candidate voted, @JsonProperty("previousHash") String previousHash,
                 @JsonProperty("voterUUID") UUID voterUUID) {
        this.index = index;
        this.timestamp = timestamp;
        this.voted = voted;
        this.previousHash = previousHash;
        this.voterUUID = voterUUID;
        this.hash = calculateHash();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    private String calculateHash() {
        String data = index + timestamp + voted.toString() + previousHash + voterUUID.toString();
        return hashService.calculateSHA256Hash(data);
    }

    public String getHash() {
        return hash;
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

    public Candidate getVoted() {
        return voted;
    }

    public UUID getVoterUUID() {
        return voterUUID;
    }

    //</editor-fold>
}
