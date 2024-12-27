package com.example.test_gen_service.controller;

import com.example.test_gen_service.service.DatabaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generate")
public class TestGeneratorController {

    @Autowired
    private DatabaseClient databaseClient;


    @PostMapping("/tests")
    public ResponseEntity<Map<String, Object>> generateTests(@RequestParam("projectName") String projectName) {
        List<Map<String, String>> methods = databaseClient.getMethods(projectName);

        if (methods.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No methods found for project: " + projectName));
        }

        StringBuilder testClass = new StringBuilder("import org.junit.jupiter.api.*;\n\n");
        testClass.append("public class GeneratedTests {\n");

        for (Map<String, String> method : methods) {
            String className = method.get("className");
            String methodName = method.get("methodName");
            String returnType = method.get("returnType");
            String parameters = method.get("parameters");

            testClass.append("\n    @Test\n");
            testClass.append("    public void test").append(methodName).append("() {\n");
            testClass.append("        // TODO: Implement test for ").append(className).append(".").append(methodName).append("(");
            testClass.append(parameters.isEmpty() ? "" : parameters);
            testClass.append(");\n");

            if (!returnType.equals("void")) {
                testClass.append("        // Example: Assert.assertEquals(expected, actual);\n");
            }

            testClass.append("    }\n");
        }

        testClass.append("}");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("generatedTests", testClass.toString());
        return ResponseEntity.ok(response);
    }

}




//
//    @PostMapping("/tests")
//    public ResponseEntity<Map<String, Object>> generateTests(@RequestParam("projectId") Long projectId) {
//        List<Map<String, String>> methods = databaseClient.getMethods(projectId);
//
//        if (methods.isEmpty()) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "No methods found for project ID: " + projectId);
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//
//        StringBuilder testClass = new StringBuilder("import org.junit.jupiter.api.*;\n\n");
//        testClass.append("public class GeneratedTests {\n");
//
//        for (Map<String, String> method : methods) {
//            String className = method.get("className");
//            String methodName = method.get("methodName");
//            String returnType = method.get("returnType");
//            String parameters = method.get("parameters");
//
//            testClass.append("\n    @Test\n");
//            testClass.append("    public void test").append(methodName).append("() {\n");
//            testClass.append("        // TODO: Implement test for ").append(className).append(".").append(methodName).append("(");
//            testClass.append(parameters.isEmpty() ? "" : parameters);
//            testClass.append(");\n");
//
//            if (!returnType.equals("void")) {
//                testClass.append("        // Example: Assert.assertEquals(expected, actual);\n");
//            }
//
//            testClass.append("    }\n");
//        }
//
//        testClass.append("}");
//
//        Map<String, Object> successResponse = new HashMap<>();
//        successResponse.put("success", true);
//        successResponse.put("generatedTestClass", testClass.toString());
//
//        return ResponseEntity.ok(successResponse);
//    }


//@RestController
//@RequestMapping("/generate")
//public class TestGeneratorController {
//
//    @Autowired
//    private DatabaseClient databaseClient;
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    @PostMapping("/tests")
//    public ResponseEntity<String> generateTestsWithAI(@RequestParam("projectId") Long projectId) {
//        // Step 1: Fetch methods from Database Service
//        List<Map<String, String>> methods = databaseClient.getMethods(projectId);
//
//        if (methods.isEmpty()) {
//            return ResponseEntity.badRequest().body("No methods found for project ID: " + projectId);
//        }
//
//        // Step 2: Call AI API to generate tests
//        String response = webClientBuilder.build()
//                .post()
//                .uri("https://api.openai.com/v1/completions") // Example: OpenAI API URL
//                .header("Authorization", "Bearer YOUR_API_KEY") // Your API key here
//                .bodyValue(Map.of(
//                        "model", "text-davinci-003",
//                        "prompt", createPrompt(methods),
//                        "max_tokens", 500
//                ))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return ResponseEntity.ok(response);
//    }
//
//    private String createPrompt(List<Map<String, String>> methods) {
//        StringBuilder prompt = new StringBuilder("Generate unit tests in Java for the following methods:\n\n");
//
//        for (Map<String, String> method : methods) {
//            String className = method.get("className");
//            String methodName = method.get("methodName");
//            String returnType = method.get("returnType");
//            String parameters = method.get("parameters");
//
//            prompt.append("Class: ").append(className).append("\n");
//            prompt.append("Method: ").append(methodName).append("\n");
//            prompt.append("Return Type: ").append(returnType).append("\n");
//            prompt.append("Parameters: ").append(parameters).append("\n\n");
//        }
//
//        prompt.append("Generate comprehensive unit tests using JUnit 5.");
//
//        return prompt.toString();
//    }
//}
