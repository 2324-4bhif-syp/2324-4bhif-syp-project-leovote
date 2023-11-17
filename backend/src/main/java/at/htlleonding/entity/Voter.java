package at.htlleonding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;

import javax.annotation.processing.Generated;

@Entity
public class Voter {
    //<editor-fold desc="Fields">
    @Id
    private Long id;
    @Column(unique=true)
    private String schoolId; // The ifxxxxxx ids every student has
    private String password; // Hash of the password (should be similar to the one in the school db)
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {

    }
    public Voter(String studentId, String password) {
        setSchoolId(studentId);
        setPassword(password);
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //</editor-fold>
}
