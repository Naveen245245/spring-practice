package com.s360.springjsp.model.email.send;

import java.util.List;

import javax.validation.constraints.Email;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmailRequest {
    @Email
    private String from;
    private List<String> toAddress;
    private List<String> cc;
    private String subject;
    private String emailBody;
}
