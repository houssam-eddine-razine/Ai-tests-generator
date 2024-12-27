package com.example.database_service.controller;

import com.example.database_service.Entity.MethodEntity;
import com.example.database_service.repository.MethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private MethodRepository repository;

    @PostMapping("/methods")
    public ResponseEntity<Void> saveMethods(@RequestBody List<MethodEntity> methods) {
        System.out.println("Received methods: " + methods);
        methods.forEach(method -> System.out.println("Project Name: " + method.getProjectName() +
                ", Class Name: " + method.getClassName() +
                ", Method Name: " + method.getMethodName()));
        repository.saveAll(methods);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/methods")
    public ResponseEntity<List<MethodEntity>> getMethodsByProject(@RequestParam("projectName") String projectName) {
        List<MethodEntity> methods = repository.findByProjectName(projectName);
        System.out.println("Retrieved methods: " + methods);
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Map<String, Object>>> listAllProjects() {
        List<MethodEntity> allMethods = repository.findAll();
        Map<String, Set<String>> projectClassesMap = new HashMap<>();

        for (MethodEntity method : allMethods) {
            projectClassesMap
                    .computeIfAbsent(method.getProjectName(), k -> new HashSet<>())
                    .add(method.getClassName());
        }

        List<Map<String, Object>> projects = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : projectClassesMap.entrySet()) {
            Map<String, Object> project = new HashMap<>();
            project.put("projectName", entry.getKey());
            project.put("classes", entry.getValue());
            projects.add(project);
        }

        return ResponseEntity.ok(projects);
    }


}

