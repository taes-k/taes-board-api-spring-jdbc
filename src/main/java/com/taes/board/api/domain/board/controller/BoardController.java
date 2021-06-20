package com.taes.board.api.domain.board.controller;

import com.taes.board.api.domain.board.dto.BoardDto;
import com.taes.board.api.domain.board.entity.Board;
import com.taes.board.api.domain.board.service.BoardService;
import com.taes.board.api.util.ConstantsUtil;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/boards")
public class BoardController
{
    private final BoardService boardService;
    private final ModelMapper modelMapper;

    @Autowired
    public BoardController(BoardService boardService, ModelMapper modelMapper)
    {
        this.boardService = boardService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/init")
    public List<BoardDto.Res> setInitBoard()
    {
        return boardService.setInitBoard().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{seq}")
    public BoardDto.Res getBoard(@PathVariable("seq") int seq)
    {
        return boardService.getBoard(seq)
            .map(this::convertToDto)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_BOARD_EXCEPTION_MSG));
    }

    @GetMapping()
    public List<BoardDto.Res> getAllBoards()
    {
        return boardService.getAllBoards().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public BoardDto.Res setBoard(@Valid @RequestBody BoardDto.CreateReq boardDto)
    {
        Board board = convertToEntity(boardDto);

        return convertToDto(boardService.setBoard(board));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{seq}")
    public BoardDto.Res updateBoard(
        @PathVariable("seq") int seq
        ,@Valid @RequestBody BoardDto.UpdateReq boardDto)
    {
        Board board = boardService.getBoard(seq)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_BOARD_EXCEPTION_MSG));

        return convertToDto(boardService.updateBoard(board, boardDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{seq}")
    public void deleteBoard(@PathVariable("seq") int seq)
    {
        boardService.deleteBoard(seq);
    }

    private Board convertToEntity(BoardDto.CreateReq boardDto)
    {
        Board board = modelMapper.typeMap(BoardDto.CreateReq.class, Board.class)
            .map(boardDto);

        log.debug("entity : {}", board);
        return board;
    }

    private BoardDto.Res convertToDto(Board board)
    {
        BoardDto.Res boardDto = modelMapper.typeMap(Board.class, BoardDto.Res.class)
            .map(board);

        log.debug("dto : {}", boardDto);
        return boardDto;
    }
}
