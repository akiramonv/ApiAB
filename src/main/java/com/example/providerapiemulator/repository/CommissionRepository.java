package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepository extends JpaRepository<Commission, String> {
}
