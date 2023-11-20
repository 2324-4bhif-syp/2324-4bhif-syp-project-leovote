package at.htlleonding.boundary;

import at.htlleonding.control.HasVotedRepository;
import at.htlleonding.entity.HasVoted;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "has-voted")
public interface HasVotedResource extends PanacheRepositoryResource<HasVotedRepository, HasVoted, Long> {

}
