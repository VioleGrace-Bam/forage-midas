package com.jpmc.midascore.component;


import com.jpmc.midascore.foundation.Incentive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;  
import org.springframework.web.client.RestTemplate;
import com.jpmc.midascore.foundation.Transaction;



@Service
public class IncentiveService {

    private final RestTemplate restTemplate;
    private final String incentiveApiUrl;


    public IncentiveService(RestTemplateBuilder builder, @Value("${incentive.api-url}") String incentiveApiUrl) {
        this.restTemplate = builder.build();
        this.incentiveApiUrl = incentiveApiUrl;
    }

    public Incentive fetchIncentive(Transaction transaction) {
        return restTemplate.postForObject(incentiveApiUrl, transaction,Incentive.class);
    }

}
