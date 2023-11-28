package at.htlleonding.entity;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Blockchain {
    protected List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
        // Genesis block
        Block genesisBlock = createGenesisBlock();
        chain.add(genesisBlock);
    }

    private Block createGenesisBlock() {
        Candidate candidate1 = new Candidate(
                "IF92837497234",
                "genesisFirstName",
                "genesisLastName",
                "genClass"
        );
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(candidate1);

        return new Block(0, System.currentTimeMillis(), candidate1 , "0");
    }

    public void addBlock(Candidate voted) {
        Block previousBlock = chain.get(chain.size() - 1);
        int index = chain.size();
        long timestamp = System.currentTimeMillis();
        String previousHash = previousBlock.getHash();
        Block newBlock = new Block(index, timestamp, voted, previousHash);
        chain.add(newBlock);
    }
}
