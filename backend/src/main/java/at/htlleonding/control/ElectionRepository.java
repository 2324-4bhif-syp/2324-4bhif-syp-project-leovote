package at.htlleonding.control;

import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ElectionRepository implements PanacheRepository<Election> {
}
