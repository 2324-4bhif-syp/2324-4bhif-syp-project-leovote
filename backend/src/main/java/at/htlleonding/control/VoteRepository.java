package at.htlleonding.control;

import at.htlleonding.entity.Vote;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VoteRepository implements PanacheRepository<Vote> {
}
