package com.eastbabel.service.impl;

import com.eastbabel.bo.email.ToEmail;
import com.eastbabel.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(ToEmail toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail.getTos());
        message.setSubject(toEmail.getSubject());
        message.setText(toEmail.getContent());
        mailSender.send(message);
    }
}
