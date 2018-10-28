package com.sivalabs.myapp.web.controller;

import com.sivalabs.myapp.utils.MockEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockserver.client.server.MockServerClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = {BaseTestContainerIntegrationTest.Initializer.class})
@Slf4j
public abstract class BaseTestContainerIntegrationTest {

    private static MockServerContainer mockServerContainer = new MockServerContainer();

    private static PostgreSQLContainer postgreSQLContainer =
        (PostgreSQLContainer)
            new PostgreSQLContainer("postgres:10.4")
                .withDatabaseName("appdb")
                .withUsername("siva")
                .withPassword("secret")
                .withStartupTimeout(Duration.ofSeconds(600));

    static {
        mockServerContainer.start();
        log.info("Starting MockServerContainer......");
        postgreSQLContainer.start();
        log.info("Starting PostgreSQLContainer......");
    }

    MockEngine mockEngine;
    private MockServerClient mockServerClient;

    @Before
    public void setUp() {
        mockServerClient = mockServerContainer.getClient();
        log.info("MockServerContainer started at " + mockServerContainer.getEndpoint());
        mockEngine = new MockEngine(mockServerClient);
        log.info("PostgreSQLContainer started on URL " + postgreSQLContainer.getJdbcUrl());
    }

    @After
    public void tearDown() throws Exception {
        mockServerClient.reset();
    }

    static class Initializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "github.host=" + mockServerContainer.getEndpoint())
                .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
