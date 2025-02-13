package at.htlleonding.control;

import at.htlleonding.entity.ChainHash;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.Optional;

@ApplicationScoped
public class ChainHashRepository implements PanacheRepository<ChainHash> {

    public void updateChainHash(String newHash, String filePath) {
        Optional<ChainHash> existingHash = find("filePath", filePath).firstResultOptional();

        if (existingHash.isPresent()) {
            ChainHash chainHash = existingHash.get();
            chainHash.setHash(newHash);
            chainHash.setTimeStamp(new Date());
            persist(chainHash);
        } else {
            // If no existing record, create a new one
            ChainHash newEntry = new ChainHash(newHash, new Date(), filePath);
            persist(newEntry);
        }
    }
}
