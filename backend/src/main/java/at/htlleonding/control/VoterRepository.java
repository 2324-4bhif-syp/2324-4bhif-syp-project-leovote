package at.htlleonding.control;

import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VoterRepository implements PanacheRepository<Voter> {

}
