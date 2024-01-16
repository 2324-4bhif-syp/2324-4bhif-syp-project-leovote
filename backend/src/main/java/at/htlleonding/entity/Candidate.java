package at.htlleonding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Candidate extends PanacheEntity {
    //<editor-fold desc="Fields">
    @Column(unique = true)
    private String schoolId;
    private String firstName;
    private String lastName;
    private String grade;
    private String pathOfImage;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Candidate() {
    }

    public Candidate(@JsonProperty("schoolId") String schoolId,
                     @JsonProperty("firstName") String firstName,
                     @JsonProperty("lastName") String lastName,
                     @JsonProperty("grade") String grade,
                     @JsonProperty("pathOfImage") String pathOfImage){
        this.schoolId = schoolId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.pathOfImage = pathOfImage;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getPathOfImage() {
        return pathOfImage;
    }
    public void setPathOfImage(String pathToImage) {
        this.pathOfImage = pathToImage;
    }
    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(schoolId, candidate.schoolId) && Objects.equals(firstName, candidate.firstName) && Objects.equals(lastName, candidate.lastName) && Objects.equals(grade, candidate.grade) && Objects.equals(pathOfImage, candidate.pathOfImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolId, firstName, lastName, grade, pathOfImage);
    }
    @Override
    public String toString() {
        return "Candidate: " +
                "Firstname: " + firstName + " " +
                "Lastname: " + lastName + " " +
                "Grade: " + grade + " " +
                "SchoolId" + schoolId + " " +
                "PathOfImage" + pathOfImage + " ";
    }
}
