package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
//Вместо того чтобы в каждый класс entity вписывать id, все будут ссылаться сюда
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
}
