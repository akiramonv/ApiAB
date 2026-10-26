package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Сервисы провайдера, таблица provider_services. Суммы хранятся строками в сомах. */
@Entity
@Table(name = "provider_services")
@Getter
@Setter
public class ProviderServiceEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "provider")
    private String providerId;

    @Column(name = "code")
    private Integer code;

    /** invoice / gns / universal */
    @Column(name = "check_source")
    private String checkSource;

    @Column(name = "categoryservice")
    private String categoryId;

    /** Фиксированная сумма услуги. */
    @Column(name = "summ")
    private String summ;

    @Column(name = "`ServicePrice`")
    private String servicePrice;

    @Column(name = "`QrCodeBase64`")
    private String qrCodeBase64;

    @Column(name = "`totalSumm`")
    private String totalSumm;

    @Column(name = "increase")
    private Boolean increase;

    @Column(name = "increasetext")
    private String increaseText;

    @Column(name = "`ServiceType`")
    private String serviceType;

    @Column(name = "`ServiceAccount`")
    private String serviceAccount;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "`ServiceCommission`")
    private String serviceCommission;

    @Column(name = "commission")
    private String commissionId;

    @Column(name = "account_type")
    private String accountTypeId;

    @Column(name = "`ServiceIdPl`")
    private String serviceIdPl;

    @Column(name = "qr_link")
    private String qrLink;
}
