package at.htlleonding.boundary;

import at.htlleonding.control.ElectionRepository;
import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "elections")
public interface ElectionResource extends PanacheRepositoryResource<ElectionRepository, Election, Long> {

}

