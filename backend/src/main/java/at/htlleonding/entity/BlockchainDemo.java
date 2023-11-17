package at.htlleonding.entity;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class BlockchainDemo {
    @Transactional
    void startUp(@Observes StartupEvent startup) {
        Blockchain blockchain = new Blockchain();

        Candidate candidate1 = new Candidate("IF200362", "Anton", "Cao", "4BHIF");
        Candidate candidate2 = new Candidate("IF200762", "Felix", "Fröller", "4BHIF");
        Election election1 = new Election("Class representative", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Crosses");
        Election election2 = new Election("School council", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Crosses");

        Vote vote1 = new Vote(candidate1.toString(), election1.toString());
        Vote vote2 = new Vote(candidate1.toString(), election1.toString());
        blockchain.addBlock(vote1);
        blockchain.addBlock(vote2);


        Vote vote3 = new Vote(candidate1.toString(), election1.toString());
        Vote vote4 = new Vote(candidate2.toString(), election2.toString());
        blockchain.addBlock(vote3);
        blockchain.addBlock(vote4);

        // Print the blockchain
        for (Block block : blockchain.chain) {
            System.out.println("Block #" + block.getHash());
            System.out.println("Votes: " + block.getVote());
            System.out.println();
        }
    }
}
