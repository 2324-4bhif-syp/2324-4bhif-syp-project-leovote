package at.htlleonding.control;

import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.CheckedTemplate;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class EmailService {
    private static final String LINK_BASE = "https://yourwebsite.com/vote";
    @Inject
    VoterRepository voterRepository;
    @Inject
    ElectionRepository electionRepository;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    static class Templates {
        public static native MailTemplateInstance invite(String link);
    }

    public Uni<Void> sendInvitesAsync(Election election) {
        List<Voter> voteCodes = voterRepository.getVoteCodesByElection(election.id);

        Multi<Void> sendInvitesMulti = Multi.createFrom().iterable(voteCodes)
                .onItem().transformToUniAndConcatenate(voter -> {
                    String link = generateLink(voter);
                    return sendInviteAsync("frfelix05@gmail.com", link, election.getName());
                });

        return sendInvitesMulti.collect().asList().onItem().ignore().andContinueWithNull();
    }

    private String generateLink(Voter voter) {
        return String.format("%s?token=%s", LINK_BASE, voter.getGeneratedId());
    }

    private Uni<Void> sendInviteAsync(String recipient, String link, String electionName) {
        String subject = "Einladung zur Wahl " + electionName;
        return Templates.invite(link).to(recipient).subject(subject).send().onItem().ignore().andContinueWithNull();
    }
}
