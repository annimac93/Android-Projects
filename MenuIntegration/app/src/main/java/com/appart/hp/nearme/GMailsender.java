package com.appart.hp.nearme;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by HP on 7/26/2017.
 */

public class GMailsender extends  javax.mail.Authenticator {

    String mailhost = "smtp.gmail.com";
    String user;
    String password;
    Session session;

    Multipart _multipart = new MimeMultipart();
    static {

        Security.addProvider(new JSSEProvider());

    }

    public GMailsender(String user, String password) {

        this.user = user;

        this.password = password;



        Properties properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);

//        props.setProperty("mail.transport.protocol", "smtp");
//
//        props.setProperty("mail.host", mailhost);
//
//        props.put("mail.smtp.auth", "true");
//
//        props.put("mail.smtp.port", "465");
//
//        props.put("mail.smtp.socketFactory.port", "465");
//
//        props.put("mail.smtp.socketFactory.class",
//
//                "javax.net.ssl.SSLSocketFactory");
//
//        props.put("mail.smtp.socketFactory.fallback", "false");
//
//        props.setProperty("mail.smtp.quitwait", "false");

//        session = Session.getDefaultInstance(props, this);
//        session = Session.getInstance(props, this);
        session = Session.getInstance(properties, this);

    }



    protected PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(user, password);

    }

    public boolean send(String subject, String body,

                        String sender, String recipients) throws Exception {
//        Properties props = _setProperties();

        if(!sender.equals("")  && !recipients.equals("") && !subject.equals("") && !body.equals("")) {
//            Session session = Session.getInstance(props, this);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(sender));

//            InternetAddress[] addressTo = new InternetAddress[recipients.length];
//            for (int i = 0; i < recipients.length; i++) {
//                addressTo[i] = new InternetAddress(_to[i]);
//            }
//            InternetAddress addressTo = new InternetAddress();
//            addressTo = new InternetAddress(recipients);
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipients));
            Log.d("&&&&&",subject);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            msg.setContent(_multipart);

            // send email
            Log.w("$#@msg",msg.toString());
            Transport.send(msg);

            return true;
        } else {
            return false;
        }
    }



    public synchronized void sendMail(String subject, String body,

                                      String sender, String recipients) throws Exception {



        try {


            MimeMessage message = new MimeMessage(session);

            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));


            message.setSender(new InternetAddress(sender));

            message.setSubject(subject);



            message.setDataHandler(handler);

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            _multipart.addBodyPart(messageBodyPart);



            // Put parts in message

            message.setContent(_multipart);



            if (recipients.indexOf(',') > 0)

                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));

            else

                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


            Transport.send(message);
            Log.d("@@@subject",subject);
            Log.d("@@@body",body);
            Log.d("@@@sender",sender);
            Log.d("@@@receiver",recipients);



        } catch (Exception e) {
            Log.e("@@@@@",e.getMessage());
        }

    }



    public void addAttachment(String filename) throws Exception {

        BodyPart messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(filename);

        messageBodyPart.setDataHandler(new DataHandler(source));

        messageBodyPart.setFileName("download image");



        _multipart.addBodyPart(messageBodyPart);

    }



    public class ByteArrayDataSource implements DataSource {

        private byte[] data;

        private String type;





        public ByteArrayDataSource(byte[] data, String type) {

            super();

            this.data = data;

            this.type = type;

        }



        public ByteArrayDataSource(byte[] data) {

            super();

            this.data = data;

        }





        public void setType(String type) {

            this.type = type;

        }



        public String getContentType() {

            if (type == null)

                return "application/octet-stream";

            else

                return type;

        }



        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(data);

        }



        public String getName() {

            return "ByteArrayDataSource";

        }



        public OutputStream getOutputStream() throws IOException {

            throw new IOException("Not Supported");

        }

    }

}
