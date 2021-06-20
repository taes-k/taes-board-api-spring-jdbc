package com.taes.board.api.domain.post;

import com.taes.board.api.domain.post.entity.BanWord;
import com.taes.board.api.domain.post.entity.Post;
import com.taes.board.api.domain.post.repository.BanWordRepository;
import com.taes.board.api.domain.post.repository.PostRepository;
import com.taes.board.api.domain.post.service.BanWordService;
import com.taes.board.api.domain.post.service.BanWordServiceImpl;
import com.taes.board.api.domain.post.service.PostService;
import com.taes.board.api.domain.post.service.PostServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class BanWordTest
{
    @Mock
    private BanWordRepository banWordRepository = Mockito.mock(BanWordRepository.class);

    private List<BanWord> givenBanWords;

    @BeforeEach
    void beforeEach_setBanWords()
    {
        String givenBanWord1Value = RandomStringUtils.random(5, true, false);
        BanWord givenBanWord1 = new BanWord();
        givenBanWord1.setWord(givenBanWord1Value);

        String givenBanWord2Value = RandomStringUtils.random(5, true, false);
        BanWord givenBanWord2 = new BanWord();
        givenBanWord2.setWord(givenBanWord2Value);

        givenBanWords = Arrays.asList(givenBanWord1, givenBanWord2);
    }

    @DisplayName("전체 금칙어 조회(paging) -> 성공")
    @Test
    void getAllBanWords_success()
    {
        // given
        BanWordService banWordService = new BanWordServiceImpl(banWordRepository);
        Mockito.doReturn(givenBanWords).when(banWordRepository).findAll();

        // when
        List<BanWord> banWords = banWordService.getAllBanWords();

        // then
        Assertions.assertTrue(CollectionUtils.isNotEmpty(banWords));
    }
}
