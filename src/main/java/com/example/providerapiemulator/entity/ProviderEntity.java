package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "providers")
@Getter
@Setter
public class ProviderEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "account")
    private String account;

    @Column(name = "code")
    private Integer code;

    @Column(name = "`Inn`")
    private String inn;

    @Column(name = "password")
    private String password;

    /** ProviderLevel / AccountLevel / ServiceLevel */
    @Column(name = "commissionlevel")
    private String commissionLevel;

    @Column(name = "commission")
    private String commissionId;

    @Column(name = "`Address`")
    private String address;

    @Column(name = "`PhoneNumber`")
    private String phoneNumber;

    @Column(name = "`PostalCode`")
    private String postalCode;

    @Column(name = "`Director`")
    private String director;

    @Column(name = "`chiefAccountant`")
    private String chiefAccountant;

    @Column(name = "`Logo`")
    private String logo;

    @Column(name = "`BIK`")
    private String bik;

    @Column(name = "`GNI`")
    private String gni;

    @Column(name = "`OKPO`")
    private String okpo;

    @Column(name = "`bankAddress`")
    private String bankAddress;

    @Column(name = "`paycodeManual`")
    private Boolean paycodeManual;

    @Column(name = "`FioInnHide`")
    private Boolean fioInnHide;

    @Column(name = "organisation")
    private String organisationId;

    @Column(name = "`LevelIdPL`")
    private String levelIdPl;

    @Column(name = "`BankFilialId`")
    private String bankFilialId;

    @Column(name = "`disableStatQr`")
    private Boolean disableStatQr;

    @Column(name = "`RequireInnForPayment`")
    private Boolean requireInnForPayment;
}
