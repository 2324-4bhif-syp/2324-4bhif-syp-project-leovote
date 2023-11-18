package at.htlleonding.boundary;

import at.htlleonding.entity.Candidate;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "candidates")
public interface CandidateResource extends PanacheEntityResource<Candidate, Long> {

}
