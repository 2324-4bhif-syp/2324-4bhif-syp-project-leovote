package at.htlleonding.control;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Date;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class EmailService {
    private static final Properties PROPERTIES = new Properties();
    private static final String EMAIL = "discord.news.bot.interface@gmail.com";
    private static final String PASSWORD = "erzp bind soza xzte";
    private static final String HOST = "smtp.gmail.com";

    static {
        PROPERTIES.put("mail.smtp.host", HOST);
        PROPERTIES.put("mail.smtp.port", "587");
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendPlainTextEmail(String to, String subject, List<String> messages, boolean debug) {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailService.EMAIL, PASSWORD);
            }
        };

        Session session = Session.getInstance(PROPERTIES, authenticator);
        session.setDebug(debug);

        try {
            // create a message with headers
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(EMAIL));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // create message body
            Multipart mp = new MimeMultipart();
            for (String message : messages) {
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setText(message, "us-ascii");
                mp.addBodyPart(mbp);
            }
            msg.setContent(mp);

            // send the message
            Transport.send(msg);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;

            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }

    // Only for testing purposes
    // TODO: Remove this method
    public static void main(String[] args) {
        sendPlainTextEmail(
                "david.spetzi@gmail.com",
                "Test Email",
                List.of("Hello", "World"),
                true
        );
    }
}
