package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Общий id для всех таблиц billing. В БД это varchar, а не uuid:
 * у части записей там числа ("1", "45"), поэтому идентификатор присваивается вручную.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
}
