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

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public String sendSimpleMail(EmailEntity emailEntity) {

        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailEntity.getRecipient());
            mailMessage.setText(emailEntity.getMsgBody());
            mailMessage.setSubject(emailEntity.getSubject());

            javaMailSender.send(mailMessage);
        }catch (Exception e){
            return "Error occurred sending email.";
        }
        return null;
    }

    @Override
    public String sendMailWithAttachment(EmailEntity emailEntity) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailEntity.getRecipient());
            mimeMessageHelper.setText(emailEntity.getMsgBody());
            mimeMessageHelper.setSubject(emailEntity.getSubject());

            FileSystemResource file
                    = new FileSystemResource(
                    new File(emailEntity.getAttachment()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";

        }catch (Exception e){
            return "Error occurred sending email.";
        }
    }
}
