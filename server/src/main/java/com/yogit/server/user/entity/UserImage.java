package com.yogit.server.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imgUUid;

    @Builder
    public UserImage(User user, String imgUUid){
        this.user = user;
        user.addImage(this);

        this.imgUUid = imgUUid;
    }
}
