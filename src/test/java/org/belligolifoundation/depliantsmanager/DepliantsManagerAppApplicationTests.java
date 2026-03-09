package org.belligolifoundation.depliantsmanager;

import org.belligolifoundation.depliantsmanager.config.ITContainersExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ITContainersExtension.class)
class DepliantsManagerAppApplicationTests {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        ITContainersExtension.configureProperties(registry);
    }

    @Test
    void contextLoads() {
        // verify context loading
    }
}
