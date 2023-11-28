package at.htlleonding.entity;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockchainDemo {
    @Transactional
    void startUp(@Observes StartupEvent startup) {

        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate1 = new Candidate("IF200362", "Anton", "Cao", "4BHIF");
        Candidate candidate2 = new Candidate("IF200762", "Felix", "Fröller", "4BHIF");
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        Election election1 = new Election("Class representative", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Crosses", candidateList);

        /*election1.getBlockchain().addBlock(candidate1);
        election1.getBlockchain().addBlock(candidate2);

        // Print the blockchain
        for (Block block : election1.getBlockchain().chain) {
            System.out.println("Block #" + block.getHash());
            System.out.println("Votes: " + block.getVote());
            System.out.println();
        }*/
    }
}
