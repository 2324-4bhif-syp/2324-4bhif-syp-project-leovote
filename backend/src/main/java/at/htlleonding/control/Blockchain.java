package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    protected List<Block> chain = new ArrayList<>();
    private final String fileName;

    public Blockchain(Election election) {
        // Genesis block
        Block genesisBlock = createGenesisBlock();
        chain.add(genesisBlock);
        fileName = election.getBlockchainFileName();
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

    public void addJson(Block addedBlock){
        // Create a JSON object
        JSONObject block = new JSONObject();
        block.put("blockHash", addedBlock.getHash());
        block.put("previousHash", addedBlock.getPreviousHash());
        block.put("timeStamp", addedBlock.getTimestamp());
        block.put("index", addedBlock.getIndex());

        JSONObject candidateVoted = new JSONObject();
        candidateVoted.put("schoolId", addedBlock.getVote().getSchoolId());
        candidateVoted.put("firstName", addedBlock.getVote().getFirstName());
        candidateVoted.put("lastName", addedBlock.getVote().getLastName());
        candidateVoted.put("grade", addedBlock.getVote().getGrade());

        block.put("voted", candidateVoted);

        try (FileWriter fileWriter = new FileWriter("src/main/resources/blockchain/" + fileName)) {
            JSONWriter writer = new JSONWriter(fileWriter);
            writer.object();
            for (String key : block.keySet()) {
                writer.key(key).value(block.get(key));
            }
            writer.endObject();
            System.out.println("JSON file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
