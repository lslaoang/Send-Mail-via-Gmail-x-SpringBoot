package com.springbootsmtp.service;

import com.springbootsmtp.model.EmailEntity;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public String sendSimpleMail(EmailEntity emailEntity) {

        try{
            LOGGER.info("Preparing email ...");
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailEntity.getRecipient());
            mailMessage.setText(emailEntity.getMsgBody());
            mailMessage.setSubject(emailEntity.getSubject());

            LOGGER.info("Sending email ...");
            javaMailSender.send(mailMessage);
            LOGGER.info("Email sent successfully!");
            return "Mail sent Successfully.";
        }catch (Exception e){
            LOGGER.severe("Sending email failed with error: " + e.getMessage());
            e.printStackTrace();
            return "Error occurred sending email. " + e.getMessage();
        }
    }

    @Override
    public String sendMailWithAttachment(EmailEntity emailEntity) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            LOGGER.info("Preparing email ...");
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailEntity.getRecipient());
            mimeMessageHelper.setText(emailEntity.getMsgBody());
            mimeMessageHelper.setSubject(emailEntity.getSubject());

            LOGGER.info("Fetching attachment ...");
            FileSystemResource file
                    = new FileSystemResource(
                    new File(emailEntity.getAttachment()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            LOGGER.info("Sending email ...");
            javaMailSender.send(mimeMessage);
            LOGGER.info("Email sent successfully!");
            return "Mail sent Successfully.";

        }catch (Exception e){
            LOGGER.severe("Sending email failed with error: " + e.getMessage());
            e.printStackTrace();
            return "Error occurred sending email. " + e.getMessage();
        }
    }
}
