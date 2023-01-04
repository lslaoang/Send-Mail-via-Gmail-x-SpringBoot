package com.springbootsmtp.controller;

import com.springbootsmtp.model.EmailEntity;
import com.springbootsmtp.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(path = "/sendMail")
    public String sendMail(@RequestBody EmailEntity email){
        String status = emailService.sendSimpleMail(email);
        return status;
    }

    @PostMapping(path = "/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailEntity email){
        String status = emailService.sendSimpleMail(email);
        return status;
    }
}
