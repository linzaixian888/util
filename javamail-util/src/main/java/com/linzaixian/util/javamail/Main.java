package com.linzaixian.util.javamail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author linzaixian
 * @since 2017-04-01 17:17:15 
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String host = "mail.berchina.com";
        String from = "abc@berchina.com";
        String port = "25";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port); 
        props.put("mail.smtp.auth", "false");

        Transport transport = null;

        try{
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

        InternetAddress to_address = new InternetAddress("lichengzhou@berchina.com");
        message.addRecipient(Message.RecipientType.TO, to_address);


        message.setSubject("a");
        message.setText("a");

        transport = session.getTransport("smtp");
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
    } finally {
        if (transport != null) try { transport.close(); } catch (MessagingException logOrIgnore){}
    }
    }
}
