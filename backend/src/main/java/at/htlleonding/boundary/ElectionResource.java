package at.htlleonding.boundary;

import at.htlleonding.control.ElectionRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;

@ResourceProperties(path = "elections")
public interface ElectionResource extends PanacheRepositoryResource<ElectionRepository, Election, Long> {
    ElectionRepository electionRepository = CDI.current().select(ElectionRepository.class).get();

    @GET
    @Path("/results/{electionId}")
    @Produces("application/json")
    default Response resultsById(
            @PathParam("electionId") Long electionId
    ) {
        Election election = Election.findById(electionId);

        if (election == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        HashMap<Candidate, Double> results = electionRepository.reviewResults(election);

        if (results.isEmpty()) {
            return Response.accepted().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }
}

