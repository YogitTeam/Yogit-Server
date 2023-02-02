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
public class Locality extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locality_id")
    private Long id;

    @OneToMany(mappedBy = "locality")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "locality")
    private List<Board> boards = new ArrayList<>();

    private String localityName;

    @Builder
    public Locality(User user, String localityName){
        this.localityName = localityName;
        this.users.add(user);
        user.addLocality(this);
    }

    public void addUser(User user){
        this.users.add(user);
        user.addLocality(this);
    }

    public void addBoard(Board board){
        this.boards.add(board);
        board.addLocality(this);
    }
}
