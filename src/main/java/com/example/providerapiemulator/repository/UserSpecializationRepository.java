package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserSpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserSpecializationRepository extends JpaRepository<UserSpecializationEntity, String> {
    List<UserSpecializationEntity> findByUserId(String userId);

    List<UserSpecializationEntity> findByUserIdIn(Collection<String> userIds);

    List<UserSpecializationEntity> findBySpecializationId(String specializationId);
}
