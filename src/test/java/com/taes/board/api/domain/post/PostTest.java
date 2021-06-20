package com.taes.board.api.domain.post;

import com.taes.board.api.domain.post.dto.PostDto;
import com.taes.board.api.domain.post.entity.Post;
import com.taes.board.api.domain.post.repository.PostRepository;
import com.taes.board.api.domain.post.service.PostService;
import com.taes.board.api.domain.post.service.PostServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("Post 유닛테스트")
public class PostTest
{
    @Mock
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private List<Post> givenPosts;

    @BeforeEach
    void beforeEach_setGivenPosts()
    {
        int givenPost1Seq = RandomUtils.nextInt() % 100000;
        Post givenPost1 = new Post();
        givenPost1.setSeq(givenPost1Seq);
        givenPost1.setTitle("TITLE");
        givenPost1.setContents("CONTENTS");

        int givenPost2Seq = RandomUtils.nextInt() % 100000;
        Post givenPost2 = new Post();
        givenPost2.setSeq(givenPost2Seq);
        givenPost2.setTitle("TITLE");
        givenPost2.setContents("CONTENTS");

        givenPosts = Arrays.asList(givenPost1, givenPost2);
    }

    @DisplayName("전체 게시물 조회(paging) -> 성공")
    @Test
    void getAllPostsWithPaging_success()
    {
        // given
        PostService postSerivce = new PostServiceImpl(20, 5, postRepository);
        Mockito.doReturn(givenPosts).when(postRepository).findAllByBoardSeq(Mockito.anyInt(), Mockito.any());

        // when
        List<Post> posts = postSerivce.getAllPostsWithPaging(1, 0);

        // then
        Assertions.assertTrue(CollectionUtils.isNotEmpty(posts));
    }

    @DisplayName("선택 게시물 조회 -> 성공")
    @Test
    void getPost_success()
    {
        // given
        Post givenPost = givenPosts.get(0);
        int givenPostSeq = givenPosts.get(0).getSeq();

        PostService postSerivce = new PostServiceImpl(20, 5, postRepository);
        Mockito.doReturn(Optional.of(givenPost)).when(postRepository).findById(Mockito.anyInt());

        // when

        Post post = postSerivce.getPost(givenPostSeq).get();
        int postSeq = post.getSeq();
        // then
        Assertions.assertEquals(givenPostSeq, postSeq);
    }

    @DisplayName("게시물 추가 -> 성공")
    @Test
    void createPosts_success()
    {
        // given
        int givenPostSeq = RandomUtils.nextInt() % 100000;
        Post givenPost = new Post();
        givenPost.setSeq(givenPostSeq);
        givenPost.setTitle("NEW_TITLE");
        givenPost.setContents("NEW_CONTENTS");

        PostService postSerivce = new PostServiceImpl(20, 5, postRepository);
        Mockito.doReturn(givenPost).when(postRepository).save(Mockito.any());

        // when
        Post post = postSerivce.setPost(givenPost);
        int postSeq = post.getSeq();

        // then
        Assertions.assertEquals(givenPostSeq, postSeq);
    }


    @DisplayName("게시물 수정 -> 성공")
    @Test
    void updatePosts_success()
    {
        // given
        Post givenPost = givenPosts.get(0);

        String updateTitle = "updated_title";
        PostDto.UpdateReq updatedPost = new PostDto.UpdateReq();
        updatedPost.setTitle(updateTitle);

        PostService postSerivce = new PostServiceImpl(20, 5, postRepository);

        // when
        Post post = postSerivce.updatePost(givenPost, updatedPost);
        String postTilte = post.getTitle();

        // then
        Assertions.assertEquals(updateTitle, postTilte);
    }

    @DisplayName("게시물 삭제 -> 성공")
    @Test
    void deletePosts_success()
    {
        // given
        int givenPostSeq = givenPosts.get(0).getSeq();

        PostService postSerivce = new PostServiceImpl(20, 5, postRepository);
        Mockito.doNothing().when(postRepository).deleteById(Mockito.anyInt());

        // when
        postSerivce.deletePost(givenPostSeq);

        // then
        Assertions.assertTrue(true);
    }
}
