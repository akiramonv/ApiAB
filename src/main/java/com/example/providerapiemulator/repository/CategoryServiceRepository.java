package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.CategoryServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryServiceRepository extends JpaRepository<CategoryServiceEntity, String> {
    List<CategoryServiceEntity> findByProviderId(String providerId);

    List<CategoryServiceEntity> findByParentCategory(String parentCategory);
}
