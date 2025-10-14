/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import com.mycompany.soundquiz.server.dto.EmailMessage;
import com.mycompany.soundquiz.server.utils.Config;
import java.util.Properties;
import javax.net.ssl.SSLSession;

import javax.mail.*;
import javax.mail.internet.*;
/**
 *
 * @author Admin
 */
public class MailService {
    private static MailService mailService;
    private final String username;
    private final String password;
    private final Properties props;
    
    private MailService() {
        this.username = Config.get("mail.username");
        this.password = Config.get("mail.password");
        
        this.props = new Properties();
        props.put("mail.smtp.host", Config.get("mail.smtp.host"));
        props.put("mail.smtp.port", Config.get("mail.smtp.port"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }
    
    public static MailService getInstance() {
        if (mailService != null) {
            return mailService;
        }
        
        mailService = new MailService();
        return mailService;
    }
    
    public EmailMessage sendMail(String toEmail, String subject, String content) {
        EmailMessage ms = new EmailMessage(false, "");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            ms.setSuccess(true);
            ms.setMessage("Mail sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            ms.setMessage("Send mail failed: " + e.getMessage());
        }
        return ms;
        
    }
}

