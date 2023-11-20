package at.htlleonding.control;

import at.htlleonding.entity.HasVoted;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HasVotedRepository implements PanacheRepository<HasVoted> {
}
