package com.taes.board.api.domain.post.validator;

import com.taes.board.api.exception.ApiException;
import com.taes.board.api.util.ConstantsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Component
public class BanWordValidator extends AbstractPatternValidator
{
    private final Pattern banWordsPattern;

    @Autowired
    public BanWordValidator(Pattern banWordsPattern)
    {
        this.banWordsPattern = banWordsPattern;
    }

    @Override
    protected Pattern getPattern()
    {
        return banWordsPattern;
    }

    @Override
    protected Consumer<String> doThrowWhenFindPattern()
    {
        return word -> {
            String message = String.format("%s (금칙어 : %s)", ConstantsUtil.PROHIBITIED_WORDS_EXCEPTION_MSG, word);
            throw new ApiException(message);
        };
    }
}
