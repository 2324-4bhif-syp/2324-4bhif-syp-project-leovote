package at.htlleonding.boundary;

import at.htlleonding.control.VoteRepository;
import at.htlleonding.entity.Vote;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "votes")
public interface VoteResource extends PanacheRepositoryResource<VoteRepository, Vote, Long> {

}
