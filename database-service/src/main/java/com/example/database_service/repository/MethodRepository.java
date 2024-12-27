package com.example.database_service.repository;

import com.example.database_service.Entity.MethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodRepository extends JpaRepository<MethodEntity, Long> {
    List<MethodEntity> findByProjectName(String projectName);
}