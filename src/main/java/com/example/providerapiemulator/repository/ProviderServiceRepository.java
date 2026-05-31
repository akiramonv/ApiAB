package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.ProviderServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProviderServiceRepository extends JpaRepository<ProviderServiceEntity, UUID> {
    List<ProviderServiceEntity> findByCategoryId(UUID categoryId);
    List<ProviderServiceEntity> findByProvId(UUID provId);
    List<ProviderServiceEntity> findByAccountId(UUID accountId);
}
