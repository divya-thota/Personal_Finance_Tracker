package com.example.splitwise;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SplitwiseService {

    @Value("${splitwise.api.url}")
    private String apiUrl;

    @Value("${splitwise.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public SplitwiseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGroups() {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment("get_groups")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}