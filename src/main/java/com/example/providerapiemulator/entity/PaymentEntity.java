package com.example.providerapiemulator.entity;

import com.example.providerapiemulator.model.CheckField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Платёж, таблица payment. Суммы (sum, "Commission", "TotalSum") хранятся строками
 * в тыйынах, у sum допустимо значение "any" - платёж на произвольную сумму.
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
public class PaymentEntity extends BaseEntity {

    @Column(name = "sum")
    private String sum;

    @Column(name = "`Commission`")
    private String commission;

    @Column(name = "`TotalSum`")
    private String totalSum;

    @Column(name = "deadline_date")
    private LocalDateTime deadlineDate;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /** in_process / success / warning / closed */
    @Column(name = "status")
    private String status;

    /** one_time / any_time */
    @Column(name = "payment_get_time")
    private String paymentGetTime;

    @Column(name = "paycode")
    private String paycode;

    /** individual / legal */
    @Column(name = "`ClientType`")
    private String clientType;

    /** ProviderLevel / AccountLevel / ServiceLevel */
    @Column(name = "`CommissionLevel`")
    private String commissionLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "source_check_db", columnDefinition = "jsonb")
    private List<CheckField> sourceCheckDb;

    /** db / gns */
    @Column(name = "source_check")
    private String sourceCheck;

    @Column(name = "invoice")
    private Boolean invoice;

    /** Автор платежа - users.id. */
    @Column(name = "provider_user")
    private String providerUserId;

    @Column(name = "`Deleted`")
    private Boolean deleted;
}
