package com.taes.board.api.domain.board;

import com.taes.board.api.common.feign.HackerNewsClient;
import com.taes.board.api.domain.board.dto.BoardDto;
import com.taes.board.api.domain.board.entity.Board;
import com.taes.board.api.domain.board.repository.BoardRepository;
import com.taes.board.api.domain.board.service.BoardService;
import com.taes.board.api.domain.board.service.BoardServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("Board 유닛테스트")
public class BoardTest
{
    @Mock
    private BoardRepository boardRepository = Mockito.mock(BoardRepository.class);

    @Mock
    private HackerNewsClient hackerNewsClient = Mockito.mock(HackerNewsClient.class);

    private List<Board> givenBoards;

    @BeforeEach
    void beforeEach_setGivenBoards()
    {
        String givenBoard1Title = RandomStringUtils.random(30, true, false);
        Board givenBoard1 = Board.builder()
            .seq(1)
            .title(givenBoard1Title)
            .build();

        String givenBoard2Title = RandomStringUtils.random(10, true, false);
        Board givenBoard2 = Board.builder()
            .seq(2)
            .title(givenBoard2Title)
            .build();

        givenBoards = Arrays.asList(givenBoard1, givenBoard2);
    }

    @DisplayName("Board 초기 세팅 -> 성공")
    @Test
    void setInitBoard_success()
    {
        // given
        HackerNewsClient.NewsItem givenNews = new HackerNewsClient.NewsItem(
            "devy"
            ,0
            ,26576943
            ,1
            ,1616651450L
            ,"Regen-COV cuts Risk of Hospitalization"
            ,"story"
            ,"https://www.empr.com/");

        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);
        Mockito.when(hackerNewsClient.getNewStories()).thenReturn(Arrays.asList(1,2,3));
        Mockito.doReturn(givenNews).when(hackerNewsClient).getNewsItemById(Mockito.any());
        Mockito.when(boardRepository.findAll()).thenReturn(null);
        Mockito.doReturn(givenBoards.get(0)).when(boardRepository).save(Mockito.any());

        // when
        List<Board> boards = boardService.setInitBoard();

        // then
        Assertions.assertTrue(CollectionUtils.isNotEmpty(boards));
    }

    @DisplayName("Board 초기 세팅 -> 실패(이미 세팅된 게시판 존재)")
    @Test
    void setInitBoard_fail_alreadySetted()
    {
        // given
        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);
        Mockito.when(boardRepository.findAll()).thenReturn(givenBoards);

        // when
        Executable exc = () -> boardService.setInitBoard();

        // then
        Assertions.assertThrows(IllegalStateException.class, exc);
    }
    @DisplayName("전체 Board 조회 -> 성공")
    @Test
    void getAllBoard_success()
    {
        // given
        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);
        Mockito.when(boardRepository.findAll()).thenReturn(givenBoards);

        // when
        List<Board> boards = boardService.getAllBoards();

        // then
        Assertions.assertTrue(CollectionUtils.isNotEmpty(boards));
    }

    @DisplayName("Board 추가 -> 성공")
    @Test
    void setBoard_success()
    {
        // given
        String givenBoardTitle = "sample_title";
        Board givenBoard = new Board();
        givenBoard.setTitle(givenBoardTitle);
        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);
        Mockito.doReturn(givenBoard).when(boardRepository).save(Mockito.any());

        // when
        Board board = boardService.setBoard(givenBoard);
        String boardTitle = board.getTitle();

        // then
        Assertions.assertEquals(givenBoardTitle, boardTitle);
    }

    @DisplayName("Board 수정 -> 성공")
    @Test
    void updateBoard_success()
    {
        // given
        Board givenBoard = givenBoards.get(0);

        String updatedTitle = "updated-title";
        BoardDto.UpdateReq updateBoard = new BoardDto.UpdateReq();
        updateBoard.setTitle(updatedTitle);

        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);

        // when
        Board board = boardService.updateBoard(givenBoard,updateBoard);
        String boardTitle = board.getTitle();

        // then
        Assertions.assertEquals(updatedTitle, boardTitle);
    }

    @DisplayName("Board 삭제 -> 성공")
    @Test
    void deleteBoard_success()
    {
        // given
        int givenBoardSeq = 1;

        BoardService boardService = new BoardServiceImpl(boardRepository, hackerNewsClient);
        Mockito.doNothing().when(boardRepository).deleteById(Mockito.anyInt());

        // when
        boardService.deleteBoard(givenBoardSeq);

        // then
        Assertions.assertTrue(true);
    }
}
