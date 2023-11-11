package at.htlleonding.entity;

import at.htlleonding.control.HashService;

import java.util.List;

class Block {
    //<editor-fold desc="Fields">
    HashService hashService = new HashService();
    private final int index;
    private final long timestamp;
    private final List<Vote> votes;
    private final String previousHash;
    private final String hash;
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
