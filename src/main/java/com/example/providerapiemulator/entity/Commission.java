package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Таблица commission: name - тип комиссии (percent/fixed), value - величина, обе колонки varchar.
 * Значения хранятся строками, поэтому парсинг вынесен в сервис.
 */
@Entity
@Table(name = "commission")
@Getter
@Setter
public class Commission extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;
}
