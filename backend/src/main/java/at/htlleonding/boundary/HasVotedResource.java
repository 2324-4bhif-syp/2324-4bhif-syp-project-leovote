package at.htlleonding.boundary;

import at.htlleonding.entity.HasVoted;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "has-voted")
public interface HasVotedResource extends PanacheEntityResource<HasVoted, Long> {

}
