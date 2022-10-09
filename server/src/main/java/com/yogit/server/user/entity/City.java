package com.yogit.server.user.entity;

import com.yogit.server.board.entity.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id;

    @OneToMany(mappedBy = "city")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    private List<Board> boards = new ArrayList<>();

    private String name;
}
