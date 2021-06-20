package com.taes.board.api.domain.board.repository;

import com.taes.board.api.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer>
{
}
