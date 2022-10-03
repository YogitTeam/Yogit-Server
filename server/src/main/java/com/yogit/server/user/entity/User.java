package com.yogit.server.user.entity;

import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.BookMark;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import lombok.*;

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

    private String loginId; // TODO 애플 로그인 성공시, 구현
    private String passWord; // TODO 애플 로그인 성공시, 구현
    private String name;
    private String profileImgUrl; // TODO image 연동 되면, 구현

    private String aboutMe;
    private String aboutMeInterest;
    private String aboutMeJob;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phoneNumber_id")
    private PhoneNumber phoneNumber; // TODO 필요한가? 문의중

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



    @Builder
    public User (String name, int age, Gender gender, Nationality nationality){
        this.name = name;
        this.age = age;
        this.memberTemp = 0;
        this.gender = gender;
        this.nationality = nationality;
    }

    public void addLanguage(Language language){
        this.languages.add(language);
    }
}
