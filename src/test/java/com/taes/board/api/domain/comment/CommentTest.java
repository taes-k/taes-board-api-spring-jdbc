package com.taes.board.api.domain.comment;

import com.taes.board.api.domain.comment.dto.CommentDto;
import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.comment.repository.CommentRepository;
import com.taes.board.api.domain.comment.service.CommentService;
import com.taes.board.api.domain.comment.service.CommentServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("Comment 유닛테스트")
public class CommentTest
{
    @Mock
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private List<Comment> givenComments;

    @BeforeEach
    void beforeEach_setGivenComments()
    {
        int givenComment1Seq = 1;
        Comment givenComment1 = new Comment();
        givenComment1.setSeq(givenComment1Seq);
        givenComment1.setContents("COMMNET_CONTENTS");
        givenComment1.setUserId("SAMPLE_USER");

        int givenComment2Seq = 2;
        Comment givenComment2 = new Comment();
        givenComment2.setSeq(givenComment2Seq);
        givenComment2.setContents("COMMNET_CONTENTS");
        givenComment2.setUserId("SAMPLE_USER");

        givenComments = Arrays.asList(givenComment1, givenComment2);
    }

    @DisplayName("게시물 전체 댓글 조회-> 성공")
    @Test
    void getAllComments_success()
    {
        // given
        CommentService commentSerivce = new CommentServiceImpl(commentRepository);
        Mockito.doReturn(givenComments).when(commentRepository).findAllByPostSeq(Mockito.anyInt());

        // when
        List<Comment> posts = commentSerivce.getAllComments(1);

        // then
        Assertions.assertTrue(CollectionUtils.isNotEmpty(posts));
    }

    @DisplayName("선택 댓글 조회 -> 성공")
    @Test
    void getComment_success()
    {
        // given
        Comment givenComment = givenComments.get(0);
        int givenCommentSeq = givenComment.getSeq();

        CommentService commentSerivce = new CommentServiceImpl(commentRepository);
        Mockito.doReturn(Optional.of(givenComment)).when(commentRepository).findById(Mockito.anyInt());

        // when
        Comment comment = commentSerivce.getComment(givenCommentSeq).get();
        int commentSeq = comment.getSeq();

        // then
        Assertions.assertEquals(givenCommentSeq, commentSeq);
    }

    @DisplayName("댓글 추가 -> 성공")
    @Test
    void createComment_success()
    {
        // given
        Comment givenComment = new Comment();
        String givenContents = "COMMNET_CONTENTS";
        givenComment.setContents(givenContents);
        givenComment.setUserId("SAMPLE_USER");

        CommentService commentSerivce = new CommentServiceImpl(commentRepository);
        Mockito.doReturn(givenComment).when(commentRepository).save(Mockito.any());

        // when
        Comment comment = commentSerivce.setComment(givenComment);
        String commentContents = comment.getContents();

        // then
        Assertions.assertEquals(givenContents, commentContents);
    }


    @DisplayName("댓글 수정 -> 성공")
    @Test
    void updateComments_success()
    {
        // given
        Comment givenComment = givenComments.get(0);

        CommentDto.UpdateReq updateComment = new CommentDto.UpdateReq();
        String givenContents = "UPDATED_COMMNET_CONTENTS";
        updateComment.setContents(givenContents);

        CommentService commentSerivce = new CommentServiceImpl(commentRepository);

        // when
        Comment comment = commentSerivce.updateComment(givenComment, updateComment);
        String commentContents = comment.getContents();

        // then
        Assertions.assertEquals(givenContents, commentContents);
    }

    @DisplayName("댓글 삭제 -> 성공")
    @Test
    void deleteComment_success()
    {
        // given
        int givenCommentSeq = givenComments.get(0).getSeq();
        CommentService commentSerivce = new CommentServiceImpl(commentRepository);
        Mockito.doNothing().when(commentRepository).deleteById(Mockito.anyInt());

        // when
        commentSerivce.deleteComment(givenCommentSeq);

        // then
        Assertions.assertTrue(true);
    }
}
