package com.yogit.server.user.entity;

import com.yogit.server.config.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @OneToMany(mappedBy = "interest")
    private List<UserInterest> userInterests = new ArrayList<>();

    // TODO : user interest 항목별로 (personality, lifestyle 등) 정해지면 엔티티에 항목 이름도 구분해서 넣기
    // @Enumerated(EnumType.STRING)
    private String name;
}
