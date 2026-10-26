package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    List<PaymentEntity> findByStatus(String status);

    List<PaymentEntity> findByProviderUserId(String providerUserId);

    List<PaymentEntity> findByPaycode(String paycode);

    /** Ссылки на провайдера в payment нет, он определяется через автора платежа. */
    List<PaymentEntity> findByProviderUserIdIn(Collection<String> providerUserIds);
}
