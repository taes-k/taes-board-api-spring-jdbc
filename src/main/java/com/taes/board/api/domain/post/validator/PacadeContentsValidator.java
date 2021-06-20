package com.taes.board.api.domain.post.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PacadeContentsValidator implements ContentsValidator
{
    private final ContentsValidator banUrlValidator;
    private final ContentsValidator banWordValidator;

    @Autowired
    public PacadeContentsValidator(
        BanUrlValidator banUrlValidator
        , BanWordValidator banWordValidator)
    {
        this.banUrlValidator = banUrlValidator;
        this.banWordValidator = banWordValidator;
    }

    public void validate(String contents)
    {
        List<ContentsValidator> validators = new ArrayList<>();

        validators.add(banUrlValidator);
        validators.add(banWordValidator);

        validators.forEach(v -> v.validate(contents));
    }
}
