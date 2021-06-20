package com.taes.board.api.domain.post.validator;

import com.taes.board.api.exception.ApiException;
import com.taes.board.api.util.ConstantsUtil;
import com.taes.board.api.util.PatternUtil;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Component
public class BanUrlValidator extends AbstractPatternValidator
{
    @Override
    protected Pattern getPattern()
    {
        return PatternUtil.BAN_URL_PATTERN;
    }

    @Override
    protected Consumer<String> doThrowWhenFindPattern()
    {
        return bannedUrl -> {
            String message = String.format("%s (금지링크 : %s)", ConstantsUtil.PROHIBITIED_URLS_EXCEPTION_MSG, bannedUrl);
            throw new ApiException(message);
        };
    }
}
