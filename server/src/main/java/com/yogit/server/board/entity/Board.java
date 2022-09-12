package com.yogit.server.board.entity;

import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User host;

    @OneToMany(mappedBy = "board")
    private List<BoardUser> boardUsers;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages;

    @OneToMany(mappedBy = "board")
    private List<BoardCategory> boardCategories;

    @OneToMany(mappedBy = "board")
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "board")
    private List<ClipBoard> clipBoards;
}
