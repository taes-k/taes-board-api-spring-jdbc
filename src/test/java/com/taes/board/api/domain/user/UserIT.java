package com.taes.board.api.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taes.board.api.IntegrationTest;
import com.taes.board.api.config.PrincipalMockMvcRequestBuilders;
import com.taes.board.api.domain.user.dto.UserDto;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User 통합 테스트")
public class UserIT extends IntegrationTest
{
    List<String> userIds = new ArrayList<>();

    @DisplayName("User1 등록")
    @BeforeEach
    void beforeEach_setUser1() throws Exception
    {
        // given
        String givenUserId = RandomStringUtils.random(10, true, false);
        UserDto.CreateReq givenUser = new UserDto.CreateReq();
        givenUser.setId(givenUserId);
        givenUser.setPassword("sample_password");
        givenUser.setName("sample_user_1");
        givenUser.setEmail("sample-email@taes.com");
        givenUser.setRoles(Collections.singletonList("USER"));

        userIds.add(givenUserId);

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/users")
            .content(new ObjectMapper().writeValueAsString(givenUser))
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("User2 등록")
    @BeforeEach
    void beforeEach_setUser2() throws Exception
    {
        // given
        String givenUserId = RandomStringUtils.random(10, true, false);
        UserDto.CreateReq givenUser = new UserDto.CreateReq();
        givenUser.setId(givenUserId);
        givenUser.setPassword("sample_password");
        givenUser.setName("sample_user_2");
        givenUser.setEmail("sample-email@taes.com");
        givenUser.setRoles(Collections.singletonList("USER"));

        userIds.add(givenUserId);

        // when - then
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.post("/users")
            .content(new ObjectMapper().writeValueAsString(givenUser))
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("전체 User 조회 -> 성공")
    @Test
    void getAllUsers_success() throws Exception
    {
        // given
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/users");

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("본인 User 정보 조회 -> 성공")
    @Test
    void getUserSelf_success() throws Exception
    {
        // given
        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.get("/users/self");

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("User 정보 수정 -> 성공")
    @Test
    void updateUser_success() throws Exception
    {
        // given
        UserDto.UpdateReq givenUser = new UserDto.UpdateReq();
        givenUser.setId(userIds.get(0));
        givenUser.setName("updated_name");
        givenUser.setEmail("updated-email@taes.com");

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.put("/users")
            .content(new ObjectMapper().writeValueAsString(givenUser))
            .contentType(MediaType.APPLICATION_JSON);

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }

    @DisplayName("User 삭제 -> 성공")
    @Test
    void deleteUser_success() throws Exception
    {
        // given
        UserDto.DeleteReq givenUser = new UserDto.DeleteReq();
        givenUser.setId(userIds.get(0));

        MockHttpServletRequestBuilder rq = PrincipalMockMvcRequestBuilders.delete("/users/" )
            .content(new ObjectMapper().writeValueAsString(givenUser))
            .contentType(MediaType.APPLICATION_JSON);;

        // when - then
        mockMvc.perform(rq)
            .andDo(log())
            .andExpect(status().isOk());
    }
}
