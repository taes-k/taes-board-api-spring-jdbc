package com.taes.board.api.domain.post.repository;

import com.taes.board.api.domain.post.entity.BanWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanWordRepository extends JpaRepository<BanWord, String>
{
}
