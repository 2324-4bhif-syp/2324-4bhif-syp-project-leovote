package at.htlleonding.control;

import at.htlleonding.entity.Election;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ElectionRepository {
    private final List<Election> elections = new ArrayList<>();

    public List<Election> getAllElections() {
        return elections;
    }

    public void addElection(Election election) {
        elections.add(election);
    }
}
