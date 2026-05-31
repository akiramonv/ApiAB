package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findByProvId(UUID provId);
}
