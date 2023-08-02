package com.s360.springjsp.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.s360.springjsp.model.email.send.EmailRequest;
import com.s360.springjsp.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService sendEmailService) {
        this.emailService = sendEmailService;
    }

    @PostMapping("/sendEmail")
    public String sendMail(@RequestBody @Valid EmailRequest emailRequest) {
        log.info("Email Object:: {}", emailRequest.toString());
        emailService.sendEmail(emailRequest);
        return null;
    }

    /**
     * @return get fromAddress,All ToAddress, All CC,Subject,Description,Multipart & filter Latest n emails, different folders
     */
    @GetMapping("/downloadEmail")
    public String sendMail() {
        // emailService.downloadEmail();
        emailService.downloadEmailUidFolder();
        return null;
    }
}
