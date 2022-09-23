package com.yogit.server.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "countryCode_id")
    private Long id;

    private int num;

    @Enumerated(EnumType.STRING)
    private Country country;
}
