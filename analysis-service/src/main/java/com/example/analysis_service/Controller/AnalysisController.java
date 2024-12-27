package com.example.analysis_service.Controller;

import com.example.analysis_service.Service.DatabaseClient;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/analyze")
public class AnalysisController {

    @Autowired
    private DatabaseClient databaseClient;

    @PostMapping("/project")
    public ResponseEntity<List<Map<String, String>>> analyzeProject(@RequestParam("file") MultipartFile file, @RequestParam("projectName") String projectName) {
        String extractDir = "extracted/" + UUID.randomUUID() + "/";
        File directory = new File(extractDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            // Étape 1 : Extraire le fichier ZIP
            extractZipFile(file, extractDir);

            // Étape 2 : Déterminer le dossier racine
            String rootFolderName = getRootFolderName(new File(extractDir));
            if (rootFolderName == null) {
                return ResponseEntity.badRequest().body(Collections.singletonList(Map.of("error", "No valid root folder found in the uploaded ZIP.")));
            }

            // Étape 3 : Analyser les fichiers Java
            String srcPath = extractDir + rootFolderName + "/src/main/java/";
            File srcFolder = new File(srcPath);
            List<Map<String, String>> methods = analyzeJavaFiles(srcFolder);

            // Étape 4 : Envoyer les méthodes extraites au service de base de données
            databaseClient.saveMethods(projectName, methods);

            // Étape 5 : Nettoyer les fichiers extraits
            cleanupDirectory(Paths.get(extractDir));

            return ResponseEntity.ok(methods);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonList(Map.of("error", "Error analyzing project: " + e.getMessage())));
        }
    }



    private void extractZipFile(MultipartFile file, String extractDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(extractDir + entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    private String getRootFolderName(File extractDir) {
        File[] files = extractDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    return file.getName();
                }
            }
        }
        return null;
    }

    private List<Map<String, String>> analyzeJavaFiles(File srcFolder) throws IOException {
        List<Map<String, String>> methods = new ArrayList<>();

        if (!srcFolder.exists() || !srcFolder.isDirectory()) {
            System.out.println("The directory 'src/main/java/' does not exist or is not a folder.");
            return methods;
        }

        for (File file : getJavaFiles(srcFolder)) {
            try {
                CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(file));
                for (ClassOrInterfaceDeclaration classDecl : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                    for (MethodDeclaration method : classDecl.getMethods()) {
                        Map<String, String> methodDetails = new HashMap<>();
                        methodDetails.put("className", classDecl.getNameAsString());
                        methodDetails.put("methodName", method.getNameAsString());
                        methodDetails.put("returnType", method.getType().asString());
                        String parameters = method.getParameters()
                                .stream()
                                .map(param -> param.getType().asString() + " " + param.getNameAsString())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("");
                        methodDetails.put("parameters", parameters);

                        methods.add(methodDetails);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error analyzing file: " + file.getName());
                e.printStackTrace();
            }
        }
        return methods;
    }

    private List<File> getJavaFiles(File dir) {
        List<File> javaFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    javaFiles.addAll(getJavaFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }

    private void cleanupDirectory(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            System.err.println("Failed to clean up directory: " + path);
            e.printStackTrace();
        }
    }
}

//    @PostMapping("/project")
//    public ResponseEntity<List<Map<String, String>>> analyzeProject(@RequestParam("file") MultipartFile file, @RequestParam("projectId") Long projectId) {
//        String extractDir = "extracted/" + UUID.randomUUID() + "/";
//        File directory = new File(extractDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        try {
//            // Step 1: Extract the ZIP file
//            extractZipFile(file, extractDir);
//
//            // Step 2: Determine root folder
//            String rootFolderName = getRootFolderName(new File(extractDir));
//            if (rootFolderName == null) {
//                return ResponseEntity.badRequest().body(Collections.singletonList(Map.of("error", "No valid root folder found in the uploaded ZIP.")));
//            }
//
//            // Step 3: Analyze Java files
//            String srcPath = extractDir + rootFolderName + "/src/main/java/";
//            File srcFolder = new File(srcPath);
//            List<Map<String, String>> methods = analyzeJavaFiles(srcFolder);
//
//            // Step 4: Send extracted methods to the Database Service
//            databaseClient.saveMethods(projectId, methods);
//
//            // Step 5: Cleanup extracted files
//            cleanupDirectory(Paths.get(extractDir));
//
//            return ResponseEntity.ok(methods);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(Collections.singletonList(Map.of("error", "Error analyzing project: " + e.getMessage())));
//        }
//    }
