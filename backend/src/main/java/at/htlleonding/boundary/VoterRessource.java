package at.htlleonding.boundary;

import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "voters")
public interface VoterRessource extends PanacheEntityResource<Voter, Long> {

}
