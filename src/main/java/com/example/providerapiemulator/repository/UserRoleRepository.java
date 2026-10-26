package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    List<UserRoleEntity> findByUserId(String userId);

    List<UserRoleEntity> findByUserIdIn(Collection<String> userIds);

    List<UserRoleEntity> findByRoleId(String roleId);
}
