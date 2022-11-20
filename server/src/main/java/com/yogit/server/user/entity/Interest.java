package com.yogit.server.user.entity;

import com.yogit.server.config.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
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

    private String name;

    public void addUserInterest(UserInterest userInterest){
        this.userInterests.add(userInterest);
    }

    @Builder
    public Interest(String name){
        this.name = name;
    }
}
