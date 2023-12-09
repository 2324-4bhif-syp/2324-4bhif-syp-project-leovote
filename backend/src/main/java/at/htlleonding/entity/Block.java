package at.htlleonding.entity;

import at.htlleonding.control.HashService;

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
    public Block(int index, Long timestamp, Candidate voted, String previousHash) {
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
        return this.voted;
    }
    //</editor-fold>
}
