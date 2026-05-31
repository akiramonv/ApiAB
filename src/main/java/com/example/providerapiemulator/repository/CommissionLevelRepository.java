package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.CommissionLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommissionLevelRepository extends JpaRepository<CommissionLevel, UUID> {

}
