package at.htlleonding.boundary;

import at.htlleonding.control.ElectionRepository;
import at.htlleonding.entity.Election;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/election")
public class ElectionRessource {
    @Inject
    ElectionRepository electionRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Election> getAll(){
        return electionRepository.getAllElections();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Election election = electionRepository.getElectionById(id);
        return election != null
                ? Response.ok(election).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        electionRepository.deleteElectionById(id);
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewElection(Election election){
        electionRepository.addElection(election);
        return Response.accepted(election).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateElection(@PathParam("id") Long id, Election updatedElection){
        Optional<Election> updated = electionRepository.updateElectionById(id, updatedElection);
        return Response.accepted(updated).build();
    }
}
