package com.example.upload_service.Controller;

import com.example.upload_service.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/test")
    public ResponseEntity<String> testUploadWithZip() {
        try {
            // Chemin absolu du fichier ZIP
            String zipFilePath = "C:\\Users\\ORIGINAL\\Downloads\\Eureka.zip";

            // Répertoire d'extraction
            String extractDir = "C:\\Users\\ORIGINAL\\Downloads\\uploaded\\extracted\\";
            File extractDirectory = new File(extractDir);
            if (!extractDirectory.exists()) {
                extractDirectory.mkdirs();
            }

            // Extraire les fichiers
            extractFilesFromZip(zipFilePath, extractDir);

            return ResponseEntity.ok("Test ZIP file extracted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Test failed due to IO error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Test failed due to unexpected error: " + e.getMessage());
        }
    }

    private void extractFilesFromZip(String zipFilePath, String extractDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new java.io.FileInputStream(zipFilePath))) {
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


}


