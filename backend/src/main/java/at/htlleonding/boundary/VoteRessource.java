package at.htlleonding.boundary;

import at.htlleonding.entity.Vote;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/vote")
public class VoteRessource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Vote> votes = Vote.listAll();
        return Response.ok(votes).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Vote.findByIdOptional(id)
                .map(vote -> Response.ok(vote).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Vote vote) {
        Vote.persist(vote);

        if (vote.isPersistent()) {
            return Response.created(URI.create("/vote" + vote.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Long id, Vote updated) {
        Vote vote = Vote.findById(id);
        if (vote == null) {
            throw new NotFoundException();
        }

        vote.election = updated.election;
        vote.candidate = updated.candidate;

        return Response.ok(vote).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
