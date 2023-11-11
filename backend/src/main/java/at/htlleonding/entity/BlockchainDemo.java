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
        Blockchain blockchain = new Blockchain();

        Candidate candidate1 = new Candidate("IF200362", "Anton", "Cao", "4BHIF");
        Candidate candidate2 = new Candidate("IF200762", "Felix", "Fröller", "4BHIF");
        Election election1 = new Election("Class representative", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Crosses");
        Election election2 = new Election("School council", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Crosses");

        // Add some votes
        List<Vote> votes1 = new ArrayList<>();

        votes1.add(new Vote(candidate1.toString(), election1.toString()));
        votes1.add(new Vote(candidate1.toString(), election1.toString()));
        blockchain.addBlock(votes1);


        List<Vote> votes2 = new ArrayList<>();
        votes2.add(new Vote(candidate1.toString(), election1.toString()));
        votes2.add(new Vote(candidate2.toString(), election2.toString()));
        blockchain.addBlock(votes2);

        // Print the blockchain
        for (Block block : blockchain.chain) {
            System.out.println("Block #" + block.getHash());
            System.out.println("Transactions: " + block.toString());
            System.out.println();
        }
    }
}
