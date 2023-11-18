package at.htlleonding.boundary;

import at.htlleonding.entity.Election;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/election")
public class ElectionRessource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Election> elections = Election.listAll();
        return Response.ok(elections).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Election.findByIdOptional(id)
                .map(voter -> Response.ok(voter).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Election election) {
        Election.persist(election);

        if (election.isPersistent()) {
            return Response.created(URI.create("/voters" + election.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Long id, Election updated) {
        Election election = Election.findById(id);
        if (election == null) {
            throw new NotFoundException();
        }

        election.name = updated.name;
        election.electionStart = updated.electionStart;
        election.electionEnd = updated.electionEnd;
        election.electionType = updated.electionType;

        return Response.ok(election).build();
    }


    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = Election.deleteById(id);

        if (deleted) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
