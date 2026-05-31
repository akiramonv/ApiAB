package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByProvId(UUID provId);
}
