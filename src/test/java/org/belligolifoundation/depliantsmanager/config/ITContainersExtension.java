package org.belligolifoundation.depliantsmanager.config;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class ITContainersExtension implements BeforeAllCallback, AfterAllCallback {

    private static final Logger logger = LoggerFactory.getLogger(ITContainersExtension.class);
    private static final boolean IS_DOCKER_AVAILABLE = isDockerAvailable();

    private static PostgreSQLContainer postgres;
    private static boolean started = false;

    private static boolean isDockerAvailable() {
        try {
            DockerClientFactory.instance().client();
            return true;
        } catch (Exception e) {
            logger.info("Docker not available: {}", e.getMessage());
            return false;
        }
    }

    public static void configureProperties(DynamicPropertyRegistry registry) {
        if (IS_DOCKER_AVAILABLE && started) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
            registry.add("spring.liquibase.user", postgres::getUsername);
            registry.add("spring.liquibase.password", postgres::getPassword);
        }
    }

    @Override
    public void beforeAll(@NotNull ExtensionContext context) {
        if (IS_DOCKER_AVAILABLE && !started) {
            postgres = new PostgreSQLContainer("postgres:18-alpine");
            if (!postgres.isRunning()) {
                postgres.start();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (postgres != null && postgres.isRunning()) {
                    postgres.stop();
                }
            }));
            started = true;
        }
    }

    @Override
    public void afterAll(@NotNull ExtensionContext context) {
        // I container vengono stoppati in automatico a fine sessione JVM
    }
}
