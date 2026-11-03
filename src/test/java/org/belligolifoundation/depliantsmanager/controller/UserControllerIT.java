package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.config.ITContainersExtension;
import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void showRegistrationForm_shouldReturnForm() throws Exception {

        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @Transactional
    void registerUser_shouldRedirectWhenUserNotExists() throws Exception {

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("username", "test3")
                        .param("password", "test3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success"));
    }

    @Test
    @Transactional
    void registerUser_withExistingId_shouldNotOverwriteExistingUser() throws Exception {

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("id", "1")
                        .param("username", "attacker")
                        .param("password", "pw")
                        .param("email", "attacker@localhost"))
                .andExpect(status().is3xxRedirection());

        User existing = userRepository.findById(1L).orElseThrow();
        assertEquals("test1", existing.getUsername());
        assertEquals("test1@localhost", existing.getEmail());
        assertFalse(passwordEncoder.matches("pw", existing.getPassword()));

        User created = userRepository.findByUsername("attacker");
        assertNotNull(created);
        assertNotEquals(Long.valueOf(1L), created.getId());
    }

    @Test
    void registerUser_withoutCsrfToken_shouldBeForbidden() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("username", "forged")
                        .param("password", "forged"))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerUser_shouldReturnFormWhenUserExists() throws Exception {

        mockMvc.perform(post("/users/register")
                        .with(csrf())
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