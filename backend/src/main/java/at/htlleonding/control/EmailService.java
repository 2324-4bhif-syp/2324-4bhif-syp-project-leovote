package at.htlleonding.control;

import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class EmailService {

    private static final String LINK_BASE = "https://yourwebsite.com/vote";
    @Inject
    Mailer mailer;

    @Inject
    VoterRepository voterRepository; // Assuming you have a VoterRepository injected

    @CheckedTemplate(requireTypeSafeExpressions = false)
    static class InviteTemplate {
        static native TemplateInstance invite(String link);
    }

    public Uni<Void> sendInvitations(Election election) {
        String subject = "Einladung zur Wahl " + election.getName();
        List<Voter> voters = voterRepository.getVoteCodesByElection(election.id);

        for (Voter voter : voters) {
            String link = generateLink(voter);
            mailer.send(Mail.withHtml("frfelix05@gmail.com", subject, InviteTemplate.invite(link).render()));
        }

        return Uni.createFrom().voidItem();
    }

    private String generateLink(Voter voter) {
        return String.format("%s?token=%s", LINK_BASE, voter.getGeneratedId());
    }
}

//@ApplicationScoped
//public class EmailService {
//    private static final String LINK_BASE = "https://yourwebsite.com/vote";
//    @Inject
//    VoterRepository voterRepository;
//    @Inject
//    ElectionRepository electionRepository;
//
//    @CheckedTemplate(requireTypeSafeExpressions = false)
//    static class Templates {
//        public static native MailTemplateInstance invite(String link);
//    }
//
//    public Uni<Void> sendInvitesAsync(Election election) {
//        List<Voter> voteCodes = voterRepository.getVoteCodesByElection(election.id);
//
//        Multi<Void> sendInvitesMulti = Multi.createFrom().iterable(voteCodes)
//                .onItem().transformToUniAndConcatenate(voter -> {
//                    String link = generateLink(voter);
//                    return sendInviteAsync("frfelix05@gmail.com", link, election.getName());
//                });
//
//        return sendInvitesMulti.collect().asList().onItem().ignore().andContinueWithNull();
//    }
//
//    private String generateLink(Voter voter) {
//        return String.format("%s?token=%s", LINK_BASE, voter.getGeneratedId());
//    }
//
//    private Uni<Void> sendInviteAsync(String recipient, String link, String electionName) {
//        String subject = "Einladung zur Wahl " + electionName;
//        return Templates.invite(link).to(recipient).subject(subject).send().onItem().ignore().andContinueWithNull();
//    }
//}
