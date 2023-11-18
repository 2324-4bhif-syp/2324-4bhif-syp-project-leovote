package at.htlleonding.boundary;

import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
@ResourceProperties(path = "elections")
public interface ElectionRessource extends PanacheEntityResource<Election, Long> {

}

