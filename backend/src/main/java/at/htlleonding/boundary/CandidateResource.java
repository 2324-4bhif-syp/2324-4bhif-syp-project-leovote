package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api")
public class CandidateResource {

    @Inject
    CandidateRepository candidateRepository;

    @GET
    @Path("/candidates")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Candidate> getAllCandidates() {
        return candidateRepository.getAllCandidates();
    }

    @POST
    @Path("/candidates")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addCandidate(Candidate candidate) {
        candidateRepository.addCandidate(candidate);
    }
}
