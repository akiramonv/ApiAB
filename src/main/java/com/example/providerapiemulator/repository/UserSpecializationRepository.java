package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserSpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSpecializationRepository extends JpaRepository<UserSpecializationEntity, UUID> {
    List<UserSpecializationEntity> findByUserId(UUID userId);
List<UserSpecializationEntity> findBySpecialId(UUID specialId);
}
