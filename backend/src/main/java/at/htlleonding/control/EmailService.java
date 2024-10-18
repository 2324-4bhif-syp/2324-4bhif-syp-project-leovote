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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class EmailService {
    private static final String LINK_BASE = "http://leovote.htl-leonding.ac.at/login";
    @Inject
    ReactiveMailer reactiveMailer;
    @Inject
    HashService hashService;
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

        Uni<List<String>> emailsUni = Uni.createFrom().item(() -> electionRepository.getVotersEmails(election.id));
        Uni<List<Voter>> votersUni = emailsUni
                .onItem().transformToUni(emails -> Uni.createFrom().item(() -> voterRepository.createVotersForElection(emails.size(), election)));

        return Uni.combine().all().unis(emailsUni, votersUni).asTuple()
                .onItem().transformToMulti(tuple -> {
                    List<String> emails = tuple.getItem1();
                    List<Voter> voters = tuple.getItem2();

                    // Create a map where each key is assigned to an email address
                    HashMap<String, Voter> voterEmailMap = IntStream.range(0, emails.size())
                            .boxed()
                            .collect(HashMap::new, (map, i) -> map.put(emails.get(i), voters.get(i)), HashMap::putAll);

                    // set the voter emailHash to the email which is the key in the hashmap
                    voterEmailMap.forEach((email, voter) -> {
                        voter.setEmailHash(hashService.calculateSHA256Hash(email));
                        voterRepository.persist(voter);
                    });

                    // Send emails asynchronously in batches
                    int batchSize = 10;
                    List<List<String>> emailBatches = new ArrayList<>();

                    for (int i = 0; i < emails.size(); i += batchSize) {
                        int end = Math.min(i + batchSize, emails.size());
                        emailBatches.add(emails.subList(i, end));
                    }

                    return sendEmailAsync(voterEmailMap, emailBatches, subject);
                });
    }

    private Multi<Void> sendEmailAsync(HashMap<String, Voter> voterEmailMap, List<List<String>> emailBatches, String subject) {
        return Multi.createFrom().iterable(emailBatches)
                .onItem().transformToUniAndConcatenate(batch -> {
                    List<Mail> mails = batch.stream()
                            .map(email -> {
                                Voter voter = voterEmailMap.get(email);
                                String link = generateLink(voter, email);
                                String html = InviteTemplate.invite(link).data("link", link).render();
                                return Mail.withHtml(email, subject, html);
                            })
                            .toList();

                    return reactiveMailer.send(mails.toArray(new Mail[0]));
                });
    }

    private String generateLink(Voter voter, String email) {
        return String.format(
                "%s?token=%s",
                LINK_BASE,
                voter.getGeneratedId()
        );
    }
}
