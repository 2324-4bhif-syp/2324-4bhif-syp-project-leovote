package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    protected List<Block> chain = new ArrayList<>();
    private final String fileName;

    public Blockchain(String electionFileName) {
        // Genesis block
        Block genesisBlock = createGenesisBlock();
        chain.add(genesisBlock);
        fileName = electionFileName;
        File file = new File(electionFileName);
        addJson(genesisBlock);
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
        addJson(newBlock);
    }

    public void addJson(Block addedBlock) {
        // Read existing JSON array from file
        List<Block> existingBlocks = readJsonArray();

        // Add the new block to the array
        existingBlocks.add(addedBlock);

        // Write the updated array back to the file
        writeJsonArray(existingBlocks);
    }

    private List<Block> readJsonArray() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/blockchain/" + fileName);

        if (!file.exists() && !file.isDirectory()) {
            // If the file doesn't exist yet, return an empty list
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Block>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading existing JSON array from file", e);
        }
    }

    private void writeJsonArray(List<Block> blocks) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/blockchain/" + fileName);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, blocks);
        } catch (IOException e) {
            throw new RuntimeException("Error writing updated JSON array to file", e);
        }
    }
}
