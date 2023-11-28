package at.htlleonding.entity;

import at.htlleonding.control.HashService;

class Block {
    //<editor-fold desc="Fields">
    private final HashService hashService = new HashService();
    private final int index;
    private final Long timestamp;
    private final Vote vote;
    private final String previousHash;
    private final String hash;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Block(int index, Long timestamp, Vote vote, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.vote = vote;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    private String calculateHash() {
        String data = index + timestamp + vote.toString() + previousHash;
        return hashService.calculateSHA256Hash(data);
    }

    public String getHash() {
        return hash;
    }

    public Vote getVote() {
        return this.vote;
    }
    //</editor-fold>
}
