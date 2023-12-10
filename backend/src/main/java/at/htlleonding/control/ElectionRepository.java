package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ElectionRepository implements PanacheRepository<Election> {
    EntityManager entityManager = Panache.getEntityManager();

    public HashMap<Candidate, Double> reviewResults (Election election){
        /*HashMap<Candidate, Double> results = new HashMap<>();
        Blockchain blockchain = new Blockchain(election.getBlockchainFileName());
        List<Block> blocks = blockchain.getBlocks();

        for(int i = 1; i < blocks.size(); i++) {
            if(results.containsKey(blocks.get(i).getVote())){
                results.get(blocks.get(i).getVote())
            }
        }*/
        return null;
    }
}
