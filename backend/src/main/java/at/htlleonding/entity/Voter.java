package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
public class Voter extends PanacheEntity {
    //<editor-fold desc="Fields">
    @Column(unique = true)
    private String schoolId; // The ifxxxxxx ids every student has
    private String password; // Hash of the password (should be similar to the one in the school db)
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter() {

    }

    public Voter(String schoolId, String password) {
        this.schoolId = schoolId;
        this.password = password;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //</editor-fold>
}
