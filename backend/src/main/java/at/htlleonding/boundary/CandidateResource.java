package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/candidates")

public class CandidateResource {

    @Inject
    CandidateRepository candidateRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCandidates() {
        return Response.ok(candidateRepository.getAllCandidates()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createCustomer(Candidate candidate, @Context UriInfo uriInfo) {
        candidateRepository.addCandidate(candidate);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(candidate.getSchoolId());
        return Response.created(uriBuilder.build()).build();
    }
}
