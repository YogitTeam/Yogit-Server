package com.yogit.server.board.entity;

import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private Integer applyStatus; // 승인 전=0, 승인 됨=1

    /*
    연관관계 편의 메서드
     */

    public BoardUser(User user, Board board) {
        this.user = user;
        this.board = board;
//        board.addBoardUser(this);
        this.applyStatus = 0;
    }

    public void changeApplyStatus(){
        this.applyStatus = 1;
    }
}
