package at.htlleonding.boundary;

import at.htlleonding.control.CandidateRepository;
import at.htlleonding.entity.Candidate;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
//import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

//import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@ResourceProperties(path = "candidates")
public interface CandidateResource extends PanacheRepositoryResource<CandidateRepository, Candidate, Long> {

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
                // Ermittle den Dateinamen
                fileName = getFileName(inputPart.getHeaders());

                // Konvertiere die hochgeladene Datei in einen InputStream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                // Konstruiere den Pfad zur hochgeladenen Datei
                String uploadDirectory = "src/main/resources/images";
                String filePath = uploadDirectory + File.separator + fileName;
                // Speichere die Datei
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
