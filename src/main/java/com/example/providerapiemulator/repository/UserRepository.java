package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findByProviderId(String providerId);
}
