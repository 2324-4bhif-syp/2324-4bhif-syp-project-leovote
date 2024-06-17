package at.htlleonding.control;

import at.htlleonding.entity.Candidate;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;

@ApplicationScoped
public class CandidateRepository implements PanacheRepository<Candidate> {
    @ConfigProperty(name = "image.path")
    String PATH_IMAGES;

    public void deleteFile(Candidate candidate) {
        String imagePath = PATH_IMAGES + candidate.getPathOfImage();
        File imageFile = new File(imagePath);

        if (imageFile.exists() && imageFile.isFile()) {
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
}
