package at.htlleonding.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Blockchain {
    protected List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
        // Genesis block
        Block genesisBlock = createGenesisBlock();
        chain.add(genesisBlock);
    }

    private Block createGenesisBlock() {
        List<Vote> votes = new ArrayList<>();
        Candidate candidate1 = new Candidate(
                "IF92837497234",
                "genesisFirstName",
                "genesisLastName",
                "genClass"
        );
        Election election1 = new Election(
                "genElection",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "genType"
        );

        votes.add(new Vote(candidate1.toString(), election1.toString()));
        return new Block(0, System.currentTimeMillis(), votes, "0");
    }

    public void addBlock(List<Vote> votes) {
        Block previousBlock = chain.get(chain.size() - 1);
        int index = chain.size();
        long timestamp = System.currentTimeMillis();
        String previousHash = previousBlock.getHash();
        Block newBlock = new Block(index, timestamp, votes, previousHash);
        chain.add(newBlock);
    }
}
