package at.htlleonding.boundary;

import at.htlleonding.entity.Vote;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "vote")
public interface VoteRessource extends PanacheEntityResource<Vote, Long> {

}
