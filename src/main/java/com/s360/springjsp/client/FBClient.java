package com.s360.springjsp.client;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.s360.springjsp.model.alok.FBResponse;
import com.s360.springjsp.model.alok.Report;

import lombok.extern.slf4j.Slf4j;

// https://data3.simplify360.com/bmapi/analytics/facebook/topposts?
// accessToken=REPORTING.jXZwRJF3Brw5TQ2T&appId=1&admin=”+admin+“&accountId=“+pageid+“&startDate=“+startDate+“&endDate=“+endDate+“
// &dummy=false&sortBy=engagement&limit=500&pagination=true&offset=1500”,timeout = 1000

@Component
@Slf4j
public class FBClient {

    private final RestTemplate restTemplate;

    // private final WebClient webClient;

    // public FBClient(WebClient webClient) {
    // this.webClient = webClient;
    // }

    // public String downloadReport(@Valid Report report) {
    // WebClient webClient = WebClient.create();
    // return webClient.post()
    // .uri("https://data3.simplify360.com/bmapi/analytics/facebook/topposts?\r\n" +
    // //
    // "accessToken=REPORTING.jXZwRJF3Brw5TQ2T&appId=1&admin=\u201D+admin+\u201C&accountId=\u201C+pageid+\u201C&startDate=\u201C+startDate+\u201C&endDate=\u201C+endDate+\u201C\r\n"
    // + //
    // "&dummy=false&sortBy=engagement&limit=500&pagination=true&offset=1500\u201D,timeout
    // = 1000")
    // .body(Mono.just(report), Report.class)
    // .retrieve()
    // .bodyToMono(String.class)
    // .toString();

    public FBClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // }
    /**
     * @param report
     * @return
     * @throws URISyntaxException
     */
    public FBResponse downloadReport(@Valid Report report){
        URI fbUrl = null;
        try {
            fbUrl = new URI(
                    "https://data3.simplify360.com/bmapi/analytics/facebook/topposts?accessToken=REPORTING.jXZwRJF3Brw5TQ2T&appId=1&admin=true&accountId=220361347991808&startDate=2023-07-01&endDate=2023-07-20&dummy=false&sortBy=engagement&limit=5&pagination=true&offset=0");
        } catch (URISyntaxException e) {
            log.error("Error :: cause :{} msg:{}",e.getCause(),e.getMessage());
        }
        FBResponse response = restTemplate.getForObject(fbUrl, FBResponse.class);
        log.info("FB Response :: {}",response.toString());
        return response;
    }

}
