package com.yogit.server.board.entity;

import com.yogit.server.board.dto.request.comment.CreateCommentReq;
import com.yogit.server.board.dto.request.comment.PatchCommentReq;
import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.config.domain.BaseStatus;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clip_board_id")
    private ClipBoard clipBoard;

    /*
    연관관계 편의 메소드
     */

    public Comment(CreateCommentReq dto, User user, ClipBoard clipBoard) {
        this.content = dto.getContent();
        this.user = user;
        this.clipBoard = clipBoard;
    }

    public void deleteComment(){
        this.setStatus(BaseStatus.INACTIVE);
    }

    public void updateComment(PatchCommentReq dto){
        this.content = dto.getContent();
    }
}
