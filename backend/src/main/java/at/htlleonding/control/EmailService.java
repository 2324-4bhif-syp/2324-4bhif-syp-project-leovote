package at.htlleonding.control;

import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
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
    Mailer mailer;
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

        // Send the emails using Mutiny's Multi
        Multi<Void> multi = Multi.createFrom().iterable(voterEmailMap.entrySet())
                .onItem().transformToUniAndMerge(entry -> sendEmail(entry.getKey(), entry.getValue(), subject));

        return multi;
    }

    private Uni<Void> sendEmail(String email, Voter voter, String subject) {
        String link = generateLink(voter);
        return Uni.createFrom().voidItem()
                .onItem().invoke(() -> mailer.send(Mail.withHtml(email, subject, InviteTemplate.invite(link).render())))
                .onItem().ignore().andContinueWithNull();
    }

    private String generateLink(Voter voter) {
        return String.format("%s?token=%s", LINK_BASE, voter.getGeneratedId());
    }
}

