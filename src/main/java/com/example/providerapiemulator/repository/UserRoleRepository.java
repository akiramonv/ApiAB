package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {
    List<UserRoleEntity> findByUserId(UUID userId);
List<UserRoleEntity> findByRoleId(UUID roleId);
}
