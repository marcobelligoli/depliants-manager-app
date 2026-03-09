package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.config.ITContainersExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(ITContainersExtension.class)
@Sql(scripts = {"classpath:sql/data.sql"})
class UserControllerIT {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        ITContainersExtension.configureProperties(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showRegistrationForm_shouldReturnForm() throws Exception {

        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUser_shouldRedirectWhenUserNotExists() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("username", "test3")
                        .param("password", "test3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success"));
    }

    @Test
    void registerUser_shouldReturnFormWhenUserExists() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("username", "test1")
                        .param("password", "any"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("userExist"));
    }

    @Test
    void showLoginForm_shouldReturnForm() throws Exception {

        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"))
                .andExpect(model().attributeExists("user"));
    }
}