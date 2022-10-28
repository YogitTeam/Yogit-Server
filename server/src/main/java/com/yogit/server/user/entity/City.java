package com.yogit.server.user.entity;

import com.yogit.server.board.entity.Board;
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
public class City extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "city_id")
    private Long id;

    @OneToMany(mappedBy = "city")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    private List<Board> boards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CityName name;

    @Builder
    public City(User user, CityName name){
        this.name = name;
        this.users.add(user);
        user.addCity(this);
    }
}
