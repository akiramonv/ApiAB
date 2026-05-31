package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.ServiceSpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceSpecializationRepository extends JpaRepository<ServiceSpecializationEntity, UUID> {
    List<ServiceSpecializationEntity> findByServiceId(UUID serviceId);
List<ServiceSpecializationEntity> findBySpecialId(UUID specialId);
}
