package com.gui9394.order;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@DirtiesContext
@AutoConfigureMockMvc
public class TestBase {

    static PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUrlParam("escapeSyntaxCallMode", "callIfNoReturn")
            .withClasspathResourceMapping("schema.sql", "docker-entrypoint-initdb.d/1.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("data.sql", "docker-entrypoint-initdb.d/2.sql", BindMode.READ_ONLY);

    @Autowired
    public MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        POSTGRES_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRES_CONTAINER.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

}
