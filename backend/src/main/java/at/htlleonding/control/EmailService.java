package at.htlleonding.control;

import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class EmailService {
    private static final String LINK_BASE = "http://89.168.107.125/login";
    @Inject
    ReactiveMailer reactiveMailer;
    @Inject
    VoterRepository voterRepository;
    @Inject
    ElectionRepository electionRepository;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    static class InviteTemplate {
        static native TemplateInstance invite(String link);
    }

    public Multi<Void> sendInvitations(Election election) {
        String subject = "Einladung zur Wahl " + election.getName();

        // Read emails for the election and generate the codes
        List<String> emails = electionRepository.getVotersEmails(election.id);
        List<Voter> voters = voterRepository.createVotersForElection(emails.size(), election);

        // Create a map where each key is assigned to an email address
        HashMap<String, Voter> voterEmailMap = IntStream.range(0, emails.size())
                .boxed()
                .collect(HashMap::new, (map, i) -> map.put(emails.get(i), voters.get(i)), HashMap::putAll);

        return Multi.createFrom().iterable(emails)
                .onItem().transformToUniAndConcatenate(email -> sendEmailAsync(email, voterEmailMap.get(email), subject));
    }

    private Uni<Void> sendEmailAsync(String email, Voter voter, String subject) {
        String link = generateLink(voter);
        String html = InviteTemplate.invite(link).data("link", link).render();

        return reactiveMailer.send(Mail.withHtml(email, subject, html));
    }

    private String generateLink(Voter voter) {
        return String.format("%s?token=%s", LINK_BASE, voter.getGeneratedId());
    }
}
