package com.example.Clinic.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.SpringServletContainerInitializer;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {
 final
 JavaMailSender mailSender;
 final
 SpringTemplateEngine springTemplateEngine;

 @Autowired
 public EmailService(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
  this.mailSender = mailSender;
  this.springTemplateEngine = springTemplateEngine;
 }

 public void sendEmail(String userEmail, String subject, Map model) {

  MimeMessage message = mailSender.createMimeMessage();
  MimeMessageHelper helper = null;
  Context context = new Context();
  context.setVariables(model);
  String html = springTemplateEngine.process("emailTemplate", context);
  String from="krakdiag@gajoss.nazwa.pl";
  try {
   helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
   helper.setFrom(from);
   helper.setTo(userEmail);
   helper.setSubject(subject);
   helper.setText(html, true);
  } catch (MessagingException e) {
   e.printStackTrace();
  }
  mailSender.send(message);
 }
}


