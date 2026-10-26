package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.ProviderServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderServiceRepository extends JpaRepository<ProviderServiceEntity, String> {
    List<ProviderServiceEntity> findByCategoryId(String categoryId);

    List<ProviderServiceEntity> findByProviderId(String providerId);

    List<ProviderServiceEntity> findByAccountTypeId(String accountTypeId);
}
