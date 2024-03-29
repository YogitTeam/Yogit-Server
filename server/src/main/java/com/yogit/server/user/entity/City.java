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
@NoArgsConstructor
public class City extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id;

    @OneToMany(mappedBy = "city")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    private List<Board> boards = new ArrayList<>();

    private String cityName;

    public void addUser(User user){
        this.users.add(user);
        user.addCity(this);
    }

    public void addBoard(Board board){
        this.boards.add(board);
        board.addCity(this);
    }

    public void changeCityName(String cityName){
        this.cityName = cityName;
    }
}
