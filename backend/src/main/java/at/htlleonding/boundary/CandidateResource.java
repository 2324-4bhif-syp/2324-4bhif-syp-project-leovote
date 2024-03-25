package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "candidates", rolesAllowed = "teacher")
public interface CandidateResource extends PanacheRepositoryResource<CandidateRepository, Candidate, Long> {

}
