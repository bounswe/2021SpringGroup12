package cmpe451.group12.beabee.login.util;

import cmpe451.group12.beabee.login.model.Users;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
@Service
public class EmailSender  {

    @Value("${spring.mail.from.email}")
    private String from;
    @Value("${spring.mail.password}")
    private String password;

    /**
     * This function sets the configurations of smtp server, calls "prepareMessage" function to create messages and sends
     * mails to mentor and mentee.
     */
    public void send(Users user) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,password);
            }
        });
        Message message = prepareMessage(session,from, user);
        Transport.send(message);
    }

    /**
     * This function creates the contents of the mail.
     */
    private static Message prepareMessage(Session session, String myAccount, Users to) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccount));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to.getEmail()));
            message.setSubject("BeABee Şifre Yenileme");
            //TODO: after deployment put our amazon link to here
            Map<String, String> env = System.getenv();
            String CLIENT_WEBPAGE_LINK = env.get("CLIENT_WEBPAGE_LINK");
            String html = "<h1> Sayin " + to.getName() + " "+ to.getSurname() + ", <br/>" + "Sifrenizi yenilemek için linkiniz: " + "<a href= "+CLIENT_WEBPAGE_LINK +"/?token="+to.getPassword_reset_token()+"> https://beabee_website_link/forgot_password/?token="+to.getPassword_reset_token()+"</a>";
            BodyPart messageBodyPart = new MimeBodyPart();
            MimeMultipart multipart = new MimeMultipart("related");
            messageBodyPart.setContent(html, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}