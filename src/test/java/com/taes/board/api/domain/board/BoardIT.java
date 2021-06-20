package com.taes.board.api.domain.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taes.board.api.IntegrationTest;
import com.taes.board.api.config.PrincipalMockMvcRequestBuilders;
import com.taes.board.api.domain.board.dto.BoardDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Board 통합 테스트")
public class BoardIT extends IntegrationTest
{
    @DisplayName("Board 초기화 등록")
    @BeforeEach
    void beforeEach_setInitBoard() throws Exception
    {
        // given
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/boards/init");

        // when - then
        mockMvc.perform(rq)
            .andDo(log());
    }

    @DisplayName("전체 Board 조회 -> 성공")
    @Test
    void getAllBoards_success() throws Exception
    {
        // given
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/boards");

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("Board 추가 -> 성공")
    @Test
    void setBaord_success() throws Exception
    {
        // given
        BoardDto.CreateReq givenBoard = new BoardDto.CreateReq();
        givenBoard.setTitle("sample_title");
        givenBoard.setContents("sample_contents");

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/boards")
            .content(new ObjectMapper().writeValueAsString(givenBoard))
            .contentType(MediaType.APPLICATION_JSON);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("Board 수정 -> 성공")
    @Test
    void updateBoard_success() throws Exception
    {
        // given
        BoardDto.CreateReq givenBoard = new BoardDto.CreateReq();
        givenBoard.setTitle("update_title");
        givenBoard.setContents("update_contents");

        int givenBoardSeq = 1;
        String url = String.format("/boards/%d", givenBoardSeq);

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.put(url)
            .content(new ObjectMapper().writeValueAsString(givenBoard))
            .contentType(MediaType.APPLICATION_JSON);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("Board 삭제 -> 성공")
    @Test
    void deleteBoard_success() throws Exception
    {
        // given
        int givenBoardSeq = 2;

        String url = String.format("/boards/%d", givenBoardSeq);

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.delete(url);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }
}
