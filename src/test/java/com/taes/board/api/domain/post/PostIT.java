package com.taes.board.api.domain.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taes.board.api.IntegrationTest;
import com.taes.board.api.config.PrincipalMockMvcRequestBuilders;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Post 통합 테스트")
public class PostIT extends IntegrationTest
{
    private static List<Integer> postSeqs = new LinkedList<>();

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

    @DisplayName("게시물 생성 -> 실패 (금칙어)")
    @Test
    void setPost_fail_banWord() throws Exception
    {
        // given
        PostDto.CreateReq givenPost = new PostDto.CreateReq();
        givenPost.setTitle("title");
        givenPost.setContents("Cotents-바보");
        givenPost.setBoardSeq(1);

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/posts")
            .content(new ObjectMapper().writeValueAsString(givenPost))
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().is5xxServerError())
            .andExpect(content().string(containsString("금칙어")));
    }

    @DisplayName("게시물 생성 -> 실패 (금지링크)")
    @Test
    void setPost_fail_banUrl() throws Exception
    {
        // given
        PostDto.CreateReq givenPost = new PostDto.CreateReq();
        givenPost.setTitle("title");
        givenPost.setContents("Cotents-https://www.naver.com");
        givenPost.setBoardSeq(1);

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/posts")
            .content(new ObjectMapper().writeValueAsString(givenPost))
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().is5xxServerError())
            .andExpect(content().string(containsString("금지된 링크")));
    }

    @DisplayName("포스팅 가능여부 확인 -> 성공")
    @Test
    void checkAPostWriteable_success() throws Exception
    {
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/posts/check/writeable");

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("전체 게시물 조회(paging) -> 성공")
    @Test
    void getAllPostsWithPaging_success() throws Exception
    {
        // given
        String givenPage = "0";

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/boards/1/posts")
            .param("page", givenPage);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("선택 게시물 조회 -> 성공")
    @Test
    void getPost_success() throws Exception
    {
        // given
        int givenPostSeq = postSeqs.get(0);
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/posts/" + givenPostSeq);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("게시물 수정 -> 성공")
    @Test
    void updatePosts_success() throws Exception
    {
        // given
        int givenPostSeq = postSeqs.get(0);
        PostDto.UpdateReq givenPost = new PostDto.UpdateReq();
        givenPost.setTitle("updated_title");

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.put("/posts/" + givenPostSeq)
            .content(new ObjectMapper().writeValueAsString(givenPost))
            .contentType(MediaType.APPLICATION_JSON);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("게시물 삭제 -> 성공")
    @Test
    void deletePosts_success() throws Exception
    {
        // given
        int givenPostSeq = postSeqs.get(0);

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.delete("/posts/" + givenPostSeq);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
        postSeqs.remove(0);
    }
}
