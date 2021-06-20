package com.taes.board.api.util;

import com.taes.board.api.domain.post.entity.BanWord;
import com.taes.board.api.domain.post.service.BanWordService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Configuration
public class BeanUtil
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

    @Bean("BanWordsPattern")
    public Pattern banWordPatterns(BanWordService banWordService)
    {
        String banWordsRegex = banWordService.getAllBanWords().stream()
            .map(BanWord::getWord)
            .collect(Collectors.joining("|"));

        log.info("Ban word regex : {}", banWordsRegex);
        return Pattern.compile(banWordsRegex);
    }
}
