package skyser.ws;

import com.sun.istack.internal.logging.Logger;
import skyser.objects.Book;
import skyser.objects.Flight;
import skyser.objects.Passenger;

import java.util.Properties;
import java.util.logging.Level;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

    static Logger logger;
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;


    public static void generate(Passenger passenger) {
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        try {
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(passenger.getEmail()));
        } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
            logger.log(Level.SEVERE,"Email was not sent", e);
       }
    }

    public static void close() {
        Transport transport = null;
        try {
            transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", "skysergroup@gmail.com", "skyser1234");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (NoSuchProviderException e) {
            logger =  Logger.getLogger(MailService.class);
            logger.log(Level.SEVERE,"Error: Provider does not exist", e);

        } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
            logger.log(Level.SEVERE,"Messaging Error", e);
        }
    }

    public static void confirmation(Passenger passenger, Flight flight, Book book){
        try {
            generateMailMessage.setSubject("Skyser | Your booking request has been accepted");
            String emailBody = "Hi,"+ passenger.getFirst_name() + " " + passenger.getLast_name() +" <br>"
                    + "We are happy to inform you that your reservation to "+flight.getArrival_location() +" has been accepted!"
                    + "<br><br> Your flight from" +flight.getDeparture_location()+ " - " +flight.getArrival_location()
                    + " on " + flight.getDeparture_date() + " for " + book.getNb_seats() + " people is now official!"
                    + "<br> Your card will be debited in the next 24 hours."
                    + "<br><br> Thanks, <br>The Skyser Team";
            generateMailMessage.setContent(emailBody, "text/html");
        } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
           logger.log(Level.SEVERE,"Messaging Error", e);
        }
   }

   public static void refusal(Passenger passenger, Flight flight, Book book){
        try{
            generateMailMessage.setSubject("Skyser | Your booking request has been declined");
            String emailBody = "Hi,"+ passenger.getFirst_name() + " " + passenger.getLast_name() +" <br>"
                    + "We are sorry to inform you that your reservation to" + flight.getArrival_location() +" has been declined. "
                    + "<br> We greatly apologise for the inconvenience that this situation may cause. "
                   + "<br><br> A refund has been issued to your credit card. While this refund is immediate on our part, <br> "
                    + "it may take up to 5 business days for the reflect on your account."
                    + "<br><br> Thanks, <br>The Skyser Team";
            generateMailMessage.setContent(emailBody, "text/html");
        } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
            logger.log(Level.SEVERE,"Messaging Error", e);
       }
    }

    public static void booking(Passenger passenger, Passenger pilot, Flight flight, Book book){
        try{
            generateMailMessage.setSubject("Skyser | You have a new booking request");
            String emailBody = "Hi,"+ pilot.getFirst_name() + " " + pilot.getLast_name() +" <br>"
                    + passenger.getFirst_name() + " " + passenger.getLast_name()+  " has requested to book your flight"
                    + flight.getDeparture_location() +" - " + flight.getArrival_location()
                    + "leaving on " + flight.getDeparture_date()
                    + "<br> Please refer to your Reservations page on the site to either confirm or deny the passenger's request."
                    + "<br><br> Remember to make this choice in a timely manner. Travelers need pilots to make booking decisions <br> "
                    + "quickly in order to have the best experience."
                    + "<br><br> Thanks, <br>The Skyser Team";
            generateMailMessage.setContent(emailBody, "text/html");
        } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
            logger.log(Level.SEVERE,"Messaging Error", e);
        }
    }

    public static void welcome(Passenger passenger){
        try{
            generateMailMessage.setSubject("Skyser | Greetings from Skyser! ");
            String emailBody = "Hi, "+ passenger.getFirst_name() + " " + passenger.getLast_name() +" <br>"
                    + "<br> Congratulations! You've successfully signed up to Skyser! "
                    + "<br> From this moment on, you'll have all your adventures on the tip of your fingers."
                    + "<br><br> Don't forget to change your status if you are a pilot wanting to share your flight with others."
                    + "<br> Just click on the Upgrade button on your Profile page."
                    + "<br><br> We're delighted to welcome you to the Skyser family."
                    + "<br><br> Thanks, <br>The Skyser Team";
            generateMailMessage.setContent(emailBody, "text/html");
      } catch (MessagingException e) {
            logger =  Logger.getLogger(MailService.class);
           logger.log(Level.SEVERE,"Messaging Error", e);
        }
    }


    public static void sendConfirmationEmail(Passenger passenger, Flight flight, Book book) {
        generate(passenger);
        confirmation(passenger, flight, book);
        close();
    }

    public static void sendRefusalEmail(Passenger passenger, Flight flight, Book book) {
        generate(passenger);
        refusal(passenger, flight, book);
       close();
    }

    public static void sendBookingEmail(Passenger passenger, Passenger pilot, Flight flight, Book book) {
       generate(passenger);
        booking(passenger, pilot, flight, book);
        close();
    }

    public static void sendWelcomeEmail(Passenger passenger) {
        generate(passenger);
        welcome(passenger);
        close();
    }

}
