package com.aiu.trips.adapter;

import org.springframework.stereotype.Service;

/**
 * SmtpEmailAdapter as per Notification.pu diagram
 * Adapter Pattern - Adapts external email service to our interface
 * 
 * Note: In production, this would use JavaMailSender from Spring Boot
 * For now, we'll use a simplified implementation
 */
@Service
public class SmtpEmailAdapter implements IEmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        // In production, use JavaMailSender:
        // MimeMessage message = mailSender.createMimeMessage();
        // message.setFrom("noreply@aiu.edu");
        // message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // message.setSubject(subject);
        // message.setText(body);
        // mailSender.send(message);
        
        // Simplified implementation for development
        System.out.println("=== EMAIL ===");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("=============");
    }
}
