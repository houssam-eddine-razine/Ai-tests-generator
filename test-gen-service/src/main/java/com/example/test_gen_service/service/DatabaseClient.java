package com.example.test_gen_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Map<String, String>> getMethods(String projectName) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8066/database/methods?projectName=" + projectName)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, String>>() {})
                .collectList()
                .block();
    }

}
