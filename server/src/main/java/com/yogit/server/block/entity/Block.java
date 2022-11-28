package com.yogit.server.block.entity;

import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocking_user_id")
    private User blockingUser; // 차단을 생성하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser; // 차단을 받는 상대 유저

    public Block(User blockingUser, User blockedUser) {
        this.blockingUser = blockingUser;
        this.blockedUser = blockedUser;
    }
}
