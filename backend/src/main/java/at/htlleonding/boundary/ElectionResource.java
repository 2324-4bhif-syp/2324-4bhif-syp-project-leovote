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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;

@ResourceProperties(path = "elections")
public interface ElectionResource extends PanacheRepositoryResource<ElectionRepository, Election, Long> {
    ElectionRepository electionRepository = CDI.current().select(ElectionRepository.class).get();

    @GET
    @Path("/results/{electionId}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response resultsById(
            @PathParam("electionId") Long electionId
    ) {
        Election election = Election.findById(electionId);

        if (election == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        HashMap<Candidate, Double> results = null;
        try {
            results = electionRepository.reviewResults(election);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Response.accepted(results).build();
    }
}

