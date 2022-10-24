package com.yogit.server.board.repository;


import com.yogit.server.board.entity.ClipBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClipBoardRepository extends JpaRepository<ClipBoard, Long> {


}
