package at.htlleonding.entity;

import at.htlleonding.control.HashService;
import jakarta.inject.Inject;

import java.util.List;

class Block {
    //<editor-fold desc="Fields">
    @Inject
    HashService hashService;
    private int index;
    private long timestamp;
    private List<Vote> votes;
    private String previousHash;
    private String hash;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Block(int index, long timestamp, List<Vote> votes, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.votes = votes;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    private String calculateHash() {
            String data = index + timestamp + votes.toString() + previousHash;
            return hashService.calculateSHA256Hash(data);
    }

    public String getHash() {
        return hash;
    }
    //</editor-fold>
}
