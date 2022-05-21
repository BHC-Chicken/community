package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards, String> {

    Boards findByBoardCode(String boardCode);
}