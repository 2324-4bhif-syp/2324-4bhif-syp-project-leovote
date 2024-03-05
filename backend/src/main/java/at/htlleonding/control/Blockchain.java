package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Voter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Blockchain {
    private static final String fileDir = "src/main/resources/blockchain/";
    private final String filePath;
    protected List<Block> chain = new ArrayList<>();

    public Blockchain(String electionFileName) {
        filePath = fileDir + electionFileName;
        File file = new File(filePath);
        if (file.exists()) {
            chain.addAll(readJsonArray());
        } else {
            // Genesis block
            Block genesisBlock = createGenesisBlock();
            chain.add(genesisBlock);
            addJson(genesisBlock);
        }
    }

    private Block createGenesisBlock() {
        Candidate candidate1 = new Candidate(
                "IF92837497234",
                "genesisFirstName",
                "genesisLastName",
                "genClass",
                "imagePath"
        );

        return new Block(0, System.currentTimeMillis(), candidate1, "0", UUID.randomUUID());
    }

    public synchronized void addBlock(Candidate voted, Voter voter) {
        Block previousBlock = chain.get(chain.size() - 1);
        int index = chain.size();
        long timestamp = System.currentTimeMillis();
        String previousHash = previousBlock.getHash();
        Block newBlock = new Block(index, timestamp, voted, previousHash, voter.getGeneratedId());
        chain.add(newBlock);
        addJson(newBlock);
    }

    public synchronized void addJson(Block addedBlock) {
        // Read existing JSON array from file
        List<Block> existingBlocks = readJsonArray();

        // Add the new block to the array
        existingBlocks.add(addedBlock);

        // Write the updated array back to the file
        writeJsonArray(existingBlocks);
    }

    private synchronized List<Block> readJsonArray() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        if (!file.exists() && !file.isDirectory()) {
            // If the file doesn't exist yet, return an empty list
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error reading existing JSON array from file", e);
        }
    }

    private synchronized void writeJsonArray(List<Block> blocks) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        Path dirPath = Paths.get(fileDir);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, blocks);
        } catch (IOException e) {
            throw new RuntimeException("Error writing updated JSON array to file", e);
        }
    }

    public List<Block> getBlocks() {
        return chain;
    }
}
