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
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.FilenameUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

@ResourceProperties(path = "candidates")
public interface CandidateResource extends PanacheRepositoryResource<CandidateRepository, Candidate, Long> {
    CandidateRepository candidateRepository = CDI.current().select(CandidateRepository.class).get();

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

        // Delete image file
        String imagePath = "src/main/resources/images/" + candidate.getPathOfImage();
        File imageFile = new File(imagePath);

        if (imageFile.exists() && imageFile.isFile()) {
            imageFile.delete();
        }

        candidate.delete();

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

        // Rename old image to new school ID

        File oldImageFile = new File("src/main/resources/images/" + candidateToUpdate.getSchoolId() + ".jpg");
        File newImageFile = new File("src/main/resources/images/" + candidate.getSchoolId() + ".jpg");

        if (oldImageFile.exists() && !newImageFile.exists()) {
            oldImageFile.renameTo(newImageFile);
        }

        candidateToUpdate.setSchoolId(candidate.getSchoolId());
        candidateToUpdate.setFirstName(candidate.getFirstName());
        candidateToUpdate.setLastName(candidate.getLastName());
        candidateToUpdate.setGrade(candidate.getGrade());
        candidateToUpdate.setPathOfImage(newImageFile.getName());

        candidateToUpdate.persist();

        return Response.ok(candidateToUpdate).build();
    }

    @GET
    @Path("images/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getCandidateImageById(@PathParam("id") Long id) {
        Candidate candidate = Candidate.findById(id);

        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String imagePath = "src/main/resources/images/" + candidate.getPathOfImage();
        System.out.println(imagePath);

        File imageFile = new File(imagePath);

        if (!imageFile.exists() || !imageFile.isFile()) {
            return Response.noContent().build();
        }

        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
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
        List<Candidate> candidates = Candidate.listAll();
        List<CandidateImageDTO> imageDTOList = new ArrayList<>();
        File folder = new File("src/main/resources/images");

        if (!folder.exists() || !folder.isDirectory()) {
            return Response.noContent().build();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return Response.noContent().build();
        }

        // Map images to candidates

        for (Candidate candidate : candidates) {
            File imageFile = new File("src/main/resources/images/" + candidate.getPathOfImage());

            if (!imageFile.exists() || !imageFile.isFile()) {
                imageFile = new File("src/main/resources/images/default.jpg");
            }

            try {
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
                imageDTOList.add(imageDTO);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                return Response.serverError().build();
            }
        }

        return Response.ok(imageDTOList).build();
    }

    @POST
    @Path("images/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    default Response uploadImage(@PathParam("id") String schoolId, MultipartFormDataInput input) {
        // Get candidate by SchoolId
        Candidate candidate = candidateRepository.findBySchoolId(schoolId);
        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Candidate with ID " + schoolId + " not found.").build();
        }

        // Validate file type
        String fileName;
        String[] allowedExtensions = new String[]{"jpg", "jpeg", "png"};

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");

        for (InputPart inputPart : inputParts) {
            try {
                fileName = schoolId + ".jpg"; // Use school ID as file name

                // Check file extension
                String fileExtension = FilenameUtils.getExtension(fileName);
                if (!Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Only JPG, JPEG, and PNG files are allowed.").build();
                }

                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                String uploadDirectory = "src/main/resources/images";
                String filePath = uploadDirectory + File.separator + fileName;
                writeFile(inputStream, filePath);
                return Response.ok().entity("Bild erfolgreich hochgeladen: " + filePath).build();
            } catch (IOException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Fehler beim Hochladen des Bildes: " + e.getMessage()).build();
            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Keine Datei hochgeladen.").build();
    }


    private String getFileName(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if (filename.trim().startsWith("filename")) {
                return filename.substring(filename.indexOf("=") + 1).trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

    private void writeFile(InputStream inputStream, String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
