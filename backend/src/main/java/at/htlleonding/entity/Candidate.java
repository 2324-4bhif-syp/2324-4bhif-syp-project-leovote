package at.htlleonding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Candidate extends PanacheEntity {
    //<editor-fold desc="Fields">
    @Column(unique = true)
    private String schoolId;
    private String firstName;
    private String lastName;
    private String grade;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Candidate() {
    }

    public Candidate(@JsonProperty("schoolId") String schoolId, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("grade") String grade) {
        this.schoolId = schoolId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
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
    //</editor-fold>
}
