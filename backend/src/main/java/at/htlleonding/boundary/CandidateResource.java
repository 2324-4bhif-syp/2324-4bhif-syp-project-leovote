package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.dto.CandidateImageDTO;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.IOException;
import java.util.*;

@ResourceProperties(path = "candidates")
public interface CandidateResource extends PanacheRepositoryResource<CandidateRepository, Candidate, Long> {
    CandidateRepository candidateRepository = CDI.current().select(CandidateRepository.class).get();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllCandidates() {
        List<Candidate> candidates = Candidate.listAll();
        return Response.ok(candidates).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response deleteCandidate(@PathParam("id") Long id) {
        Candidate candidate = Candidate
                .findById(id);

        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        candidateRepository.deleteFile(candidate);
        return Response.ok().build();
    }

    // Overide the default panache endpoint of put

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    default Response updateCandidate(@PathParam("id") Long id, Candidate candidate) {
        Candidate candidateToUpdate = Candidate.findById(id);

        if (candidateToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        candidateRepository.updateFile(candidate, candidateToUpdate);
        return Response.ok(candidateToUpdate).build();
    }

    @GET
    @Path("images/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getCandidateImageById(@PathParam("id") Long id) {
        try {
            CandidateImageDTO imageDTO = candidateRepository.getImageByCandidateId(id);
            if(imageDTO == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (imageDTO.imagePath() == null) {
                return Response.noContent().build();
            }
            return Response.ok(imageDTO).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Path("images")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllCandidateImages() {
        try{
            return Response.ok(candidateRepository.getImagesForCandidates()).build();
        }  catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("images/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    default Response uploadImage(@PathParam("id") String schoolId, MultipartFormDataInput input) {
        try {
            String message = candidateRepository.createImage(schoolId, input);
            if (message.contains("Bild erfolgreich hochgeladen:")) {
                return Response.ok().entity(message).build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message).build();
        } catch (IOException exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Fehler beim Hochladen des Bildes: " + exception.getMessage()).build();
        }
    }
}
