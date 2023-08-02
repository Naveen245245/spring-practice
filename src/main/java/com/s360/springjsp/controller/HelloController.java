package com.s360.springjsp.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.s360.springjsp.client.FBClient;
import com.s360.springjsp.model.alok.FBResponse;
import com.s360.springjsp.model.alok.Report;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class HelloController {

    private final FBClient fBClient;

    public HelloController(FBClient fBClient) {
        this.fBClient = fBClient;
    }

    @GetMapping("/hello")
    public String greeString(Model model){
        model.addAttribute("dateKey", new Date());
        return "Index";
    }

    @GetMapping("/showForm")
	public String showForm(Report report) {
		return "Greeting";
	}

    @PostMapping("/clientCall")
    public FBResponse addUser(@Valid Report report, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Error msg ::{}",result.hasErrors() );
        }
        log.info("Fetched form details :{}",report.toString());
        return fBClient.downloadReport(report);
    }
    
}
