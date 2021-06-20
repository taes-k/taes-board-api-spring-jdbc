package com.taes.board.api.domain.post.repository;

import com.taes.board.api.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer>
{
    List<Post> findAllByBoardSeq(int boardSeq, Pageable pageable);

    @Query(value = "select count(*) from post where user_id=:userId and reg_dt >= CURDATE() AND reg_dt < (CURDATE() + INTERVAL 1 DAY)",
        nativeQuery = true)
    Long countByUserIdAndCurrentDate(String userId);
}
