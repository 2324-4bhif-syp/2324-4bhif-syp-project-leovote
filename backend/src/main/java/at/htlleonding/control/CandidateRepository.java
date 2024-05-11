package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateRepository implements PanacheRepository<Candidate> {
    public Candidate findBySchoolId(String schoolId) {
        return find("schoolId", schoolId).firstResult();
    }
}
