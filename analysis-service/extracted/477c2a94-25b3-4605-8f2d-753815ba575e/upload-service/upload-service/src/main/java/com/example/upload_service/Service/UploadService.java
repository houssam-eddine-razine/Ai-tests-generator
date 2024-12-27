package com.example.upload_service.Service;

import com.example.upload_service.Entity.UploadedProject;
import com.example.upload_service.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadService {
    @Autowired
    private ProjectRepository repository;

    public void saveExtractedFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    UploadedProject project = new UploadedProject();
                    project.setFileName(file.getName());
                    project.setFilePath(file.getAbsolutePath());
                    repository.save(project);
                }
            }
        }
    }
}


