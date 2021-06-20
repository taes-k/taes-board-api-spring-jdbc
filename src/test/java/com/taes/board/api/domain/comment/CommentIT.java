package com.taes.board.api.domain.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taes.board.api.IntegrationTest;
import com.taes.board.api.config.PrincipalMockMvcRequestBuilders;
import com.taes.board.api.domain.comment.dto.CommentDto;
import com.taes.board.api.domain.post.dto.PostDto;
import com.taes.board.api.util.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Comment 통합 테스트")
public class CommentIT extends IntegrationTest
{
    private static List<Integer> postSeqs = new LinkedList<>();
    private static List<Integer> commentSeqs = new LinkedList<>();

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

    @DisplayName("게시물 생성 -> 성공")
    @BeforeEach
    void beforeEach_setPost() throws Exception
    {
        // given
        PostDto.CreateReq givenPost = new PostDto.CreateReq();
        givenPost.setTitle("TITLE");
        givenPost.setContents("CONTENTS");
        givenPost.setBoardSeq(1);

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/posts")
            .content(new ObjectMapper().writeValueAsString(givenPost))
            .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(rq)
            .andDo(log())
            .andReturn();

        if (result.getResponse().getStatus() == 200)
        {
            String contents = result.getResponse().getContentAsString();
            PostDto.Res res = ObjectMapperUtil.getMapper().readValue(contents, PostDto.Res.class);
            postSeqs.add(res.getSeq());
        }
    }

    @DisplayName("댓글 생성 -> 성공")
    @BeforeEach
    void beforeEach_setComment() throws Exception
    {
        // given
        CommentDto.CreateReq givenComment = new CommentDto.CreateReq();
        givenComment.setContents("CONTENTS");

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/posts/1/comments")
            .content(new ObjectMapper().writeValueAsString(givenComment))
            .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(rq)
            .andDo(log())
            .andReturn();

        if (result.getResponse().getStatus() == 200)
        {
            String contents = result.getResponse().getContentAsString();
            CommentDto.Res res = ObjectMapperUtil.getMapper().readValue(contents, CommentDto.Res.class);
            commentSeqs.add(res.getSeq());
        }
    }

    @DisplayName("전체 댓글 조회 -> 성공")
    @Test
    void getAllComments_success() throws Exception
    {
        // given
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/posts/1/comments");

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("댓글 수정 -> 성공")
    @Test
    void updateComment_success() throws Exception
    {
        // given
        int givenCommentSeq = commentSeqs.get(0);
        CommentDto.UpdateReq givenComment = new CommentDto.UpdateReq();
        givenComment.setContents("updated_contents");

        MockHttpServletRequestBuilder rq =
            PrincipalMockMvcRequestBuilders.put("/posts/1/comments/" + givenCommentSeq)
                .content(new ObjectMapper().writeValueAsString(givenComment))
                .contentType(MediaType.APPLICATION_JSON);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("댓글 삭제 -> 성공")
    @Test
    void deleteComments_success() throws Exception
    {
        // given
        int givenCommentSeq = commentSeqs.get(0);

        MockHttpServletRequestBuilder rq =
            PrincipalMockMvcRequestBuilders.delete("/posts/1/comments/" + givenCommentSeq);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
        commentSeqs.remove(0);
    }
}
