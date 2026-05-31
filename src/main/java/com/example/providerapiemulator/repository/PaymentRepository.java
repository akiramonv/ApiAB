package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.PaymentEntity;
import com.example.providerapiemulator.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findByProvId(UUID provId);

    List<PaymentEntity> findByStatus(PaymentStatus status);
}
