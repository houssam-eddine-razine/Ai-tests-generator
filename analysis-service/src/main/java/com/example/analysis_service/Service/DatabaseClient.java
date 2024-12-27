package com.example.analysis_service.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

//    public void saveMethods(Long projectId, List<Map<String, String>> methods) {
//        // Transform the methods list into List<Map<String, Object>>
//        List<Map<String, Object>> methodEntities = methods.stream()
//                .map(method -> {
//                    Map<String, Object> entity = new HashMap<>();
//                    entity.put("projectId", projectId);
//                    entity.put("className", method.get("className"));
//                    entity.put("methodName", method.get("methodName"));
//                    entity.put("returnType", method.get("returnType"));
//                    entity.put("parameters", method.get("parameters"));
//                    return entity;
//                })
//                .collect(Collectors.toList());
//
//        // Send data to the Database Service
//        webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8066/database/methods")
//                .bodyValue(methodEntities)
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
//    }
        public void saveMethods(String projectName, List<Map<String, String>> methods) {
            // Transformer la liste des méthodes en List<Map<String, Object>>
            List<Map<String, Object>> methodEntities = methods.stream()
                    .map(method -> {
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("projectName", projectName); // Utilisation de projectName
                        entity.put("className", method.get("className"));
                        entity.put("methodName", method.get("methodName"));
                        entity.put("returnType", method.get("returnType"));
                        entity.put("parameters", method.get("parameters"));
                        return entity;
                    })
                    .collect(Collectors.toList());

            // Envoyer les données au service de base de données
            webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8066/database/methods")
                    .bodyValue(methodEntities)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }

}

