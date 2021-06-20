package com.taes.board.api.config;

import com.taes.board.api.domain.board.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Order(2)
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner
{
    private final BoardService boardService;

    @Autowired
    public ApplicationRunner(BoardService boardService)
    {
        this.boardService = boardService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        try
        {
            boardService.setInitBoard();
        }
        catch (Exception e)
        {
            log.warn("서버 초기구성실패, 서비스는 정상작동됩니다.", e);
        }

    }
}
