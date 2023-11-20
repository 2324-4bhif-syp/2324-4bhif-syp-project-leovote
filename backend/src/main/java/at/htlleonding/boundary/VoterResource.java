package at.htlleonding.boundary;

import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "voters")
public interface VoterResource extends PanacheRepositoryResource<VoterRepository, Voter, Long> {

}
