package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
public class Candidate extends PanacheEntity {
    //<editor-fold desc="Fields">
    @Column(unique = true)
    public String schoolId;
    public String firstName;
    public String lastName;
    public String grade;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Candidate() {

    }

    public Candidate(String schoolId, String firstName, String lastName, String grade) {
        this.schoolId = schoolId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }
    //</editor-fold>
}
