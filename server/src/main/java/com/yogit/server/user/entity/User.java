package com.yogit.server.user.entity;

import com.yogit.server.board.entity.*;
import com.yogit.server.user.dto.request.editUserEssentialProfileReq;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
    private City city;

    @OneToMany(mappedBy = "user")
    private List<BoardUser> boardUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserInterest> userInterests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Language> languages = new ArrayList<>();

    private String loginId; // TODO 애플 로그인 성공시, 구현
    private String passWord; // TODO 애플 로그인 성공시, 구현
    private String name;
    private String profileImg; // 프로필 대표 이미지 TODO image 연동 되면, 구현

    private String aboutMe;
    private String aboutMeInterest;
    private String aboutMeJob;

    private Integer age;
    private float memberTemp;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClipBoard> clipBoard = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserImage> userImages = new ArrayList<>();

    @Builder
    public User (String name, int age, Gender gender, Nationality nationality){
        this.name = name;
        this.age = age;
        this.memberTemp = 0;
        this.gender = gender;
        this.nationality = nationality;
        this.status = UserStatus.ACTIVE;
    }

    public void addLanguage(Language language){
        this.languages.add(language);
    }

    public void changeUserInfo(editUserEssentialProfileReq editUserEssentialProfileReq){
        if(editUserEssentialProfileReq.getUserName() != null) this.name = editUserEssentialProfileReq.getUserName();
        if(editUserEssentialProfileReq.getUserAge() != null) this.age = editUserEssentialProfileReq.getUserAge();
        if(editUserEssentialProfileReq.getGender() != null) this.gender = editUserEssentialProfileReq.getGender();
        if(editUserEssentialProfileReq.getNationality() != null) this.nationality = editUserEssentialProfileReq.getNationality();
    }

    public void delUser(){
        this.name = null;
        this.profileImg = null;
        this.status = UserStatus.DELETE;
    }
}
