package at.htlleonding.control;

import at.htlleonding.entity.Block;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class VoterRepository implements PanacheRepository<Voter> {
    EntityManager entityManager = Panache.getEntityManager();

    // Creates voters, which are able to vote in elections contained in the election List
    public List<Voter> createVotersForElection(int count, Election election){
        List<Voter> votersList = new ArrayList<>();
        for(int i = 0; i < count; i++){
            Voter voterToAdd = new Voter(election);
            votersList.add(voterToAdd);
            Voter.persist(voterToAdd);
        }
        return votersList;
    }

    // Logic for the vote.
    // Vote passes, if candidate exists, voter hasn't voted, voter is able to vote in election
    // and election exists
    public boolean voteForCandidate(Voter voter, Candidate candidate, Election election){
        Election election1 = Election.findById(election.id);
        Optional<Candidate> candidate1 = election1.getParticipatingCandidates()
                .stream()
                .filter(candidate2 -> candidate2 == candidate)
                .findFirst();
        Blockchain blockchain = new Blockchain(election1.getBlockchainFileName());
        if(candidate1.isPresent() && election1 != null
                && voter.getParticipatingIn() == election1 &&
        election1.getElectionStart().isBefore(LocalDateTime.now()) &&
        election1.getElectionEnd().isAfter(LocalDateTime.now()) &&
        !hasAlreadyVoted(blockchain, voter)
        ){
            entityManager.merge(voter);
            blockchain.addBlock(candidate, voter);
            return true;
        }
        return false;
    }

    public boolean hasAlreadyVoted(Blockchain blockchain, Voter voter){
        for(Block block : blockchain.getBlocks()){
            if(block.getVoterUUID().equals(voter.getGeneratedId())){
                return true;
            }
        }
        return false;
    }
}
