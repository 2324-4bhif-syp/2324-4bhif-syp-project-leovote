package at.htlleonding.entity;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

public class BlockChainTest {
    @Transactional
    void startUp(@Observes StartupEvent event) {
        String genesisTransactions = "IF200176";
        Block genesisBlock = new Block(0, genesisTransactions);
        System.out.println("One Block:");
        System.out.println(genesisBlock.getBlockHash());
        String secondTransaction = "IF20011111";
        Block secondBlock = new Block(genesisBlock.getBlockHash(), secondTransaction);
        System.out.println("With annother Block:");
        System.out.println(secondBlock.getBlockHash());
    }
}
