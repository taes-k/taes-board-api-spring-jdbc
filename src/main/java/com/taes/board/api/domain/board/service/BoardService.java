package com.taes.board.api.domain.board.service;

import com.taes.board.api.domain.board.dto.BoardDto;
import com.taes.board.api.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardService
{
    List<Board> setInitBoard();

    Optional<Board> getBoard(int seq);

    List<Board> getAllBoards();

    Board setBoard(Board board);

    Board updateBoard(Board board, BoardDto.UpdateReq boardDto);

    void deleteBoard(int seq);
}
