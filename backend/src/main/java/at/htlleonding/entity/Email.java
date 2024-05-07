package at.htlleonding.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Email extends PanacheEntity {

    private String email;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Election election;

    public Email(String email, Election election) {
        this.email = email;
        this.election = election;
    }

    public Email() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
}
