package at.htlleonding.boundary;

import at.htlleonding.control.AuthorizationService;
import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.dto.CandidateImageDTO;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.IOException;
import java.util.*;

import static at.htlleonding.boundary.Roles.ADMIN_ROLE;
import static at.htlleonding.boundary.Roles.USER_ROLE;

@Path("candidates")
public class CandidateResource {
    @Inject
    CandidateRepository candidateRepository;

    @Inject
    AuthorizationService authorizationService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/all")
    @WithSpan("getAllCandidates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCandidates() {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        List<Candidate> candidates = Candidate.listAll();
        return Response.ok(candidates).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCandidateById(@PathParam("id") Long id) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        Candidate candidate = Candidate.findById(id);
        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(candidate).build();
    }

    @GET
    @Path("getBySchoolId/{schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCandidatesBySchoolId(@PathParam("schoolId") String schoolId) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        Candidate candidate = candidateRepository.getCandidateBySchoolId(schoolId);
        return Response.ok(candidate).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCandidate(@PathParam("id") Long id) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id must not be null").build();
        }

        Candidate candidate = Candidate
                .findById(id);

        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        candidateRepository.deleteFile(candidate);
        return Response.ok().build();
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateCandidate(@PathParam("id") Long id, Candidate candidate) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        Candidate candidateToUpdate = Candidate.findById(id);

        if (candidateToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity with id " + id + " not found").build();
        }
        candidateRepository.updateFile(candidate, candidateToUpdate);
        return Response.ok(candidateToUpdate).build();
    }

    @GET
    @Path("images/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCandidateImageById(@PathParam("id") Long id) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        try {
            CandidateImageDTO imageDTO = candidateRepository.getImageByCandidateId(id);
            if (imageDTO == null) {
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
    public Response getAllCandidateImages() {
        if (!authorizationService.hasAccess(jwt, USER_ROLE) && !authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        try {
            return Response.ok(candidateRepository.getImagesForCandidates()).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("images/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadImage(@PathParam("id") String schoolId, MultipartFormDataInput input) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCandidate(Candidate candidate) {
        if (!authorizationService.hasAccess(jwt, ADMIN_ROLE)) {
            return Response.status(403).build();
        }

        Candidate createdCandidate = candidateRepository.createCandidate(candidate);
        return Response.accepted(createdCandidate).build();
    }
}
