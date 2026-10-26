package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<SpecializationEntity, String> {
    List<SpecializationEntity> findByProviderId(String providerId);
}
