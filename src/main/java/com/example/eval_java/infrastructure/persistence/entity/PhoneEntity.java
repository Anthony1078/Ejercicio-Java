package com.example.eval_java.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "phones")
public class PhoneEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable=false)
    private String number;
    @Column(nullable=false)
    private String citycode;
    @Column(nullable=false)
    private String contrycode;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private UserEntity user;

}
