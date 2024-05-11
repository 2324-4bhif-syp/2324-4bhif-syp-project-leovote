package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.dto.CandidateImageDTO;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
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
    @GET
    @Path("images/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getCandidateImageById(@PathParam("id") Long id) {
        Candidate candidate = Candidate.findById(id);

        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String imagePath = "src/main/resources/images/" + candidate.getPathOfImage();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                return Response.noContent().build();
            }

            byte[] imageBytes = inputStream.readAllBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
            return Response.ok(imageDTO).build();
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return Response.serverError().build();
        }
    }

    @GET
    @Path("images")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllCandidateImages() {
        List<Candidate> candidates = Candidate.listAll();
        Map<String, Candidate> candidateImageMap = new HashMap<>();

        for (Candidate candidate : candidates) {
            candidateImageMap.put(candidate.getPathOfImage(), candidate);
        }

        List<CandidateImageDTO> imageDTOList = new ArrayList<>();
        File folder = new File("src/main/resources/images");

        if (!folder.exists() || !folder.isDirectory()) {
            return Response.noContent().build();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return Response.noContent().build();
        }

        for (File file : files) {
            if (file.isFile()) {
                Candidate candidate = candidateImageMap.get(file.getName());
                if (candidate != null) {
                    try {
                        byte[] imageBytes = Files.readAllBytes(file.toPath());
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
                        imageDTOList.add(imageDTO);
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle or log the exception appropriately
                        return Response.serverError().build();
                    }
                }
            }
        }

        return Response.ok(imageDTOList).build();
    }

    @POST
    @Path("images/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    default Response uploadImage(@PathParam("id") Long id, MultipartFormDataInput input) {
        // Get candidate by ID
        Candidate candidate = Candidate.findById(id);
        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Candidate with ID " + id + " not found.").build();
        }

        // Validate file type
        String fileName;
        String[] allowedExtensions = new String[]{"jpg", "jpeg", "png"};

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");

        for (InputPart inputPart : inputParts) {
            try {
                fileName = candidate.getSchoolId() + ".jpg"; // Use school ID as file name

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
