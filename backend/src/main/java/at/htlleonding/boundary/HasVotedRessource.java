package at.htlleonding.boundary;

import at.htlleonding.entity.HasVoted;
import at.htlleonding.entity.HasVotedId;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "hasVoted")
public interface HasVotedRessource extends PanacheEntityResource<HasVoted, HasVotedId> {

}
