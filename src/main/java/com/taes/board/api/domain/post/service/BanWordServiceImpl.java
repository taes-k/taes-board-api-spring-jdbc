package com.taes.board.api.domain.post.service;

import com.taes.board.api.domain.post.entity.BanWord;
import com.taes.board.api.domain.post.repository.BanWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanWordServiceImpl implements BanWordService
{
    private BanWordRepository banWordRepository;

    @Autowired
    public BanWordServiceImpl(BanWordRepository banWordRepository)
    {
        this.banWordRepository = banWordRepository;
    }

    @Override
    public List<BanWord> getAllBanWords()
    {
        return banWordRepository.findAll();
    }
}
