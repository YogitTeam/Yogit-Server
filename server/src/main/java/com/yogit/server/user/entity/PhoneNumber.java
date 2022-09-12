package com.yogit.server.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "phoneNumber_id")
    private Long id;

    @OneToOne(mappedBy = "phoneNumber")
    private User user;

    private int contryCode;
    private int areaCode;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PhoneValidStatus phoneValidStatus;
}
