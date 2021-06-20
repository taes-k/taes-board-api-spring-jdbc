package com.taes.board.api.domain.board.service;

import com.taes.board.api.common.feign.HackerNewsClient;
import com.taes.board.api.domain.board.dto.BoardDto;
import com.taes.board.api.domain.board.entity.Board;
import com.taes.board.api.domain.board.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService
{
    private final BoardRepository boardRepository;
    private final HackerNewsClient hackerNewsClient;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, HackerNewsClient hackerNewsClient)
    {
        this.boardRepository = boardRepository;
        this.hackerNewsClient = hackerNewsClient;
    }

    @Transactional
    public List<Board> setInitBoard()
    {
        List<Board> boards = boardRepository.findAll();
        if (CollectionUtils.isNotEmpty(boards))
        {
            throw new IllegalStateException("이미 존재하는 게시판이 있습니다.");
        }

        List<Board> initBoards = getRecentNews().stream()
            .map(news -> new Board(news.getTitle()))
            .peek(boardRepository::save)
            .collect(Collectors.toList());

        log.info("초기 게시판 구성이 완료되었습니다.");
        return initBoards;
    }

    private List<HackerNewsClient.NewsItem> getRecentNews()
    {
        List<Integer> recentNews = hackerNewsClient.getNewStories();

        log.debug("recent news from hacker news : {}", recentNews);
        return hackerNewsClient.getNewStories().stream()
            .limit(10)
            .map(hackerNewsClient::getNewsItemById)
            .collect(Collectors.toList());
    }

    public Optional<Board> getBoard(int seq)
    {
        return boardRepository.findById(seq);
    }

    public List<Board> getAllBoards()
    {
        return boardRepository.findAll();
    }

    @Transactional
    public Board setBoard(Board board)
    {
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Board board, BoardDto.UpdateReq boardDto)
    {
        updateBoardInfo(board, boardDto);
        return board;
    }

    private void updateBoardInfo(Board board, BoardDto.UpdateReq boardDto)
    {
        if (StringUtils.isNotEmpty(boardDto.getTitle()))
        {
            board.setTitle(boardDto.getTitle());
        }

        if (StringUtils.isNotEmpty(boardDto.getContents()))
        {
            board.setContents(boardDto.getContents());
        }
    }

    @Transactional
    public void deleteBoard(int seq)
    {
        boardRepository.deleteById(seq);
    }
}
