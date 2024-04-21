package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.dto.CandidateImageDTO;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@ResourceProperties(path = "candidates")
public interface CandidateResource extends PanacheRepositoryResource<CandidateRepository, Candidate, Long> {
    CandidateRepository candidateRepository = CDI.current().select(CandidateRepository.class).get();

    @GET
    @Path("/images")
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllCandidateImages() throws IOException {
        List<Candidate> candidates = candidateRepository.listAll();
        System.out.println(candidates.size());
        List<CandidateImageDTO> imageDTOList = new ArrayList<>();
        File folder = new File("src/main/resources/images");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for(Candidate candidate : candidates) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if (candidate.getPathOfImage().equals(file.getName())) {
                                byte[] imageBytes = Files.readAllBytes(file.toPath());
                                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                                CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
                                imageDTOList.add(imageDTO);
                            }
                        }
                    }
                }
            }
        }
        return Response.ok(imageDTOList).build();
    }

    @POST
    @Path("/uploadImage")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    default Response uploadImage(MultipartFormDataInput input) {
        String fileName = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");

        for (InputPart inputPart : inputParts) {
            try {
                fileName = getFileName(inputPart.getHeaders());

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
