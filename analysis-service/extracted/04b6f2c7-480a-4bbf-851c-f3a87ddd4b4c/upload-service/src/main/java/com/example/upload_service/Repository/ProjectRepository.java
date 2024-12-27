package com.example.upload_service.Repository;

import com.example.upload_service.Entity.UploadedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<UploadedProject, Long> {}

