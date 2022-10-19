package com.yogit.server.board.entity;

import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.config.domain.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String imgUUid;

    @Builder
    public BoardImage(Board board, String imgUUid) {
        this.board = board;
        board.addBoardImage(this);
        this.imgUUid = imgUUid;
    }

    /*
    연관관계 편의 메서드
     */
    public void deleteBoardImage(){
        this.setStatus(BaseStatus.INACTIVE);
    }
}
