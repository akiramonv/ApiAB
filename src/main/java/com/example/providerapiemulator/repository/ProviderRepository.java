package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<ProviderEntity, UUID> {

}
