package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.dto.CandidateImageDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ApplicationScoped
public class CandidateRepository implements PanacheRepository<Candidate> {
    @ConfigProperty(name = "image.path")
    String PATH_IMAGES;

    @ConfigProperty(name = "default.image.path")
    String DEFAULT_IMAGE;

    public void deleteFile(Candidate candidate) {
        String imagePath = PATH_IMAGES + candidate.getPathOfImage();
        File imageFile = new File(imagePath);

        if (imageFile.exists() && imageFile.isFile() && !imageFile.getName().equals(DEFAULT_IMAGE)) {
            imageFile.delete();
        }

        candidate.delete();
    }

    public void updateFile(Candidate candidate, Candidate candidateToUpdate) {
        File oldImageFile = new File(PATH_IMAGES + candidateToUpdate.getSchoolId() + ".jpg");
        File newImageFile = new File(PATH_IMAGES + candidate.getSchoolId() + ".jpg");

        if (oldImageFile.exists() && !newImageFile.exists()) {
            oldImageFile.renameTo(newImageFile);
        }

        candidateToUpdate.setSchoolId(candidate.getSchoolId());
        candidateToUpdate.setFirstName(candidate.getFirstName());
        candidateToUpdate.setLastName(candidate.getLastName());
        candidateToUpdate.setGrade(candidate.getGrade());
        candidateToUpdate.setPathOfImage(newImageFile.getName());

        candidateToUpdate.persist();
    }

    public Candidate getCandidateBySchoolId(String schoolId) {
        return find("schoolId", schoolId).firstResult();
    }

    public List<CandidateImageDTO> getImagesForCandidates() throws IOException {
        List<Candidate> candidates = Candidate.listAll();
        List<CandidateImageDTO> imageDTOList = new ArrayList<>();
        File folder = new File(PATH_IMAGES);

        if (!folder.exists() || !folder.isDirectory()) {
            return new ArrayList<>();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        // Map images to candidates

        for (Candidate candidate : candidates) {
            File imageFile = new File(PATH_IMAGES + candidate.getPathOfImage());

            byte[] imageBytes;
            if (!imageFile.exists() || !imageFile.isFile()) {
                imageBytes = loadDefaultImageBytes();
            } else {
                imageBytes = Files.readAllBytes(imageFile.toPath());
            }

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            CandidateImageDTO imageDTO = new CandidateImageDTO(candidate.id, base64Image);
            imageDTOList.add(imageDTO);
        }
        return imageDTOList;
    }

    public CandidateImageDTO getImageByCandidateId(Long id) throws IOException {
        Candidate candidate = Candidate.findById(id);

        if (candidate == null) {
            return null;
        }

        String imagePath = PATH_IMAGES + candidate.getPathOfImage();
        File imageFile = new File(imagePath);

        if (!imageFile.exists() || !imageFile.isFile()) {
            String imagePathDefault = PATH_IMAGES + candidate.getPathOfImage();
            File imageFileDefault = new File(imagePathDefault);

            byte[] imageBytes;
            if (!imageFileDefault.exists() || !imageFileDefault.isFile()) {
                imageBytes = loadDefaultImageBytes();
            } else {
                imageBytes = Files.readAllBytes(imageFileDefault.toPath());
            }

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return new CandidateImageDTO(candidate.id, base64Image);
        }

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return new CandidateImageDTO(candidate.id, base64Image);
    }

    public String createImage(String schoolId, MultipartFormDataInput input) throws IOException {
        ensureImagesDirectoryExists();

        // Validate file type
        String fileName;
        String[] allowedExtensions = new String[]{"jpg", "jpeg", "png"};

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");

        for (InputPart inputPart : inputParts) {
            fileName = schoolId + ".jpg"; // Use school ID as file name

            // Check file extension
            String fileExtension = FilenameUtils.getExtension(fileName);
            if (!Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())) {
                return "Only JPG, JPEG, and PNG files are allowed.";
            }

            InputStream inputStream = inputPart.getBody(InputStream.class, null);

            String filePath = PATH_IMAGES + File.separator + fileName;
            writeFile(inputStream, filePath);
            return "Bild erfolgreich hochgeladen: " + filePath;
        }
        return "Keine Datei hochgeladen.";
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

    public Candidate createCandidate(Candidate candidate) {
        candidate.persist();
        return candidate;
    }

    private byte[] loadDefaultImageBytes() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_IMAGE)) {
            if (is == null) {
                throw new IOException("Default image not found in classpath!");
            }
            return is.readAllBytes();
        }
    }

    private void ensureImagesDirectoryExists() throws IOException {
        Path path = Paths.get(PATH_IMAGES);
        if (!Files.exists(path)) {
            Files.createDirectories(path); // Creates the directory and any missing parent directories
        }
    }
}
