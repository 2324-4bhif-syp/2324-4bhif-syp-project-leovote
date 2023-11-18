package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
public class Voter extends PanacheEntity {
    @Column(unique = true)
    public String schoolId; // The ifxxxxxx ids every student has
    public String password; // Hash of the password (should be similar to the one in the school db)

    public Voter() {

    }

    public Voter(String schoolId, String password) {
        this.schoolId = schoolId;
        this.password = password;
    }
}
