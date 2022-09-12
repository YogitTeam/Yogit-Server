package com.yogit.server.user.entity;

import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.BookMark;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
    private City city;

    @OneToMany(mappedBy = "user")
    private List<BoardUser> boardUsers;

    @OneToMany(mappedBy = "user")
    private List<UserInterest> userInterests;

    @OneToMany(mappedBy = "user")
    private List<Language> languages;

    private String loginId;
    private String passWord;
    private String name;
    private String profileImgUrl;

    private String aboutMe;
    private String aboutMeInterest;
    private String aboutMeJob;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phoneNumber_id")
    private PhoneNumber phoneNumber;

    private int age;
    private float memberTemp;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "user")
    private List<ClipBoard> clipBoards;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
}
