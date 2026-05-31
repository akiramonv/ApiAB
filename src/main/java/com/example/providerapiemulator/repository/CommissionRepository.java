package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {

}
