package at.htlleonding.boundary;

import at.htlleonding.entity.Voter;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/voters")
public class VoterRessource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Voter> voters = Voter.listAll();
        return Response.ok(voters).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Voter.findByIdOptional(id)
                .map(voter -> Response.ok(voter).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Voter voter) {
        Voter.persist(voter);

        if (voter.isPersistent()) {
            return Response.created(URI.create("/voters" + voter.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = Voter.deleteById(id);

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
        long deletedId = Voter.delete("schoolId", schoolId);

        if (deletedId != -1) {
            return Response.ok().entity("{\"deletedId\": " + deletedId + "}").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
