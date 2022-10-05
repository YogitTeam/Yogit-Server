package com.yogit.server.board.entity;

import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private String title;

    private String address;

    private float longitute;

    private float latitude;

    private LocalDateTime date; // 모임 시각

    private String notice;

    private String introduction; // 게시글 내용 상세설명

    private String kindOfPerson; // 이런 사람을 원합니다 설명 글.

    private int currentMember;

    private int totalMember;

    @OneToMany(mappedBy = "board")
    private List<BoardUser> boardUsers;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages;

    @OneToOne(mappedBy = "board")
    private BoardCategory boardCategory;

    @OneToMany(mappedBy = "board")
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "board")
    private List<ClipBoard> clipBoards;


    // 생성자
    @Builder
    public Board(Long id, City city, User host, String title, String address, float longitute, float latitude, LocalDateTime date, String notice, String introduction, String kindOfPerson, int currentMember, int totalMember, List<BoardUser> boardUsers, List<BoardImage> boardImages, BoardCategory boardCategory, List<BookMark> bookMarks, List<ClipBoard> clipBoards) {
        this.id = id;
        this.city = city;
        this.host = host;
        this.title = title;
        this.address = address;
        this.longitute = longitute;
        this.latitude = latitude;
        this.date = date;
        this.notice = notice;
        this.introduction = introduction;
        this.kindOfPerson = kindOfPerson;
        this.currentMember = currentMember;
        this.totalMember = totalMember;
        this.boardUsers = boardUsers;
        this.boardImages = boardImages;
        this.boardCategory = boardCategory;
        this.bookMarks = bookMarks;
        this.clipBoards = clipBoards;
    }

    /*
    연관관계 편의 메서드
     */

    public void changeBoardCurrentMember(int currentMember){
        this.currentMember = currentMember;
    }
}
