package com.yogit.server.user.entity;

import com.yogit.server.config.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "country_code_id")
    private Long id;

    private int num;

    @Enumerated(EnumType.STRING)
    private Country country;
}
