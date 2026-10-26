package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.ServiceSpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ServiceSpecializationRepository extends JpaRepository<ServiceSpecializationEntity, String> {
    List<ServiceSpecializationEntity> findByServiceId(String serviceId);

    List<ServiceSpecializationEntity> findByServiceIdIn(Collection<String> serviceIds);

    List<ServiceSpecializationEntity> findBySpecializationId(String specializationId);

    List<ServiceSpecializationEntity> findBySpecializationIdIn(Collection<String> specializationIds);
}
