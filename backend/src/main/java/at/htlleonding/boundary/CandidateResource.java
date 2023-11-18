package at.htlleonding.boundary;

import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Voter;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/candidates")
public class CandidateResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Candidate> candidates = Candidate.listAll();
        return Response.ok(candidates).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Candidate.findByIdOptional(id)
                .map(candidate -> Response.ok(candidate).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("school-id/{school-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("school-id") String id) {
        return Candidate.find("schoolId", id)
                .singleResultOptional()
                .map(candidate -> Response.ok(candidate).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Candidate candidate) {
        Candidate.persist(candidate);

        if (candidate.isPersistent()) {
            return Response.created(URI.create("/candidates" + candidate.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = Candidate.deleteById(id);

        if (deleted) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Transactional
    @Path("school-id/{school-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBySchoolId(@PathParam("school-id") String schoolId) {
        long deletedId = Candidate.delete("schoolId", schoolId);

        if (deletedId != -1) {
            return Response.ok().entity("{\"deletedId\": " + deletedId + "}").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
