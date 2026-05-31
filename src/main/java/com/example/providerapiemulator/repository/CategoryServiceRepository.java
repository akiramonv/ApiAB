package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.CategoryServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryServiceRepository extends JpaRepository<CategoryServiceEntity, UUID> {
    List<CategoryServiceEntity> findByProvId(UUID provId);
List<CategoryServiceEntity> findByPrntCategory(UUID prntCategory);
}
