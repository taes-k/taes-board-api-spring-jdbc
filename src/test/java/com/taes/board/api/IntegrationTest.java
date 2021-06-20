package com.taes.board.api;

import com.taes.board.api.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTest
{
    @Autowired
    protected MockMvc mockMvc;

}
