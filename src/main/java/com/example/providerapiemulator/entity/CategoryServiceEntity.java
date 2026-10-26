package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Папки сервисов провайдера, таблица provider_service_categoryes. */
@Entity
@Table(name = "provider_service_categoryes")
@Getter
@Setter
public class CategoryServiceEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "parent_category")
    private String parentCategory;

    @Column(name = "icon")
    private String icon;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "`RelatedProvider`")
    private String providerId;
}
