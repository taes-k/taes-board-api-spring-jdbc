package com.taes.board.api.domain.post.validator;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public abstract class AbstractPatternValidator implements ContentsValidator
{

    @Override
    public void validate(String contents)
    {
        findPatternInContents(contents)
            .ifPresent(doThrowWhenFindPattern());
    }

    private Optional<String> findPatternInContents(String contents)
    {
        if (StringUtils.isEmpty(getPattern().pattern()) || StringUtils.isEmpty(contents))
            return Optional.empty();

        Matcher matcher = getPattern().matcher(contents);

        if (matcher.find())
            return Optional.of(matcher.group());

        return Optional.empty();
    }

    protected abstract Pattern getPattern();

    protected abstract Consumer<String> doThrowWhenFindPattern();


}
