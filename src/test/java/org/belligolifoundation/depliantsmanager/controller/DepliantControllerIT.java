package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.config.ITContainersExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(ITContainersExtension.class)
@Sql(scripts = {"classpath:sql/data.sql"})
class DepliantControllerIT {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        ITContainersExtension.configureProperties(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test2")
    void listDepliants_shouldReturnListView() throws Exception {

        mockMvc.perform(get("/depliants"))
                .andExpect(status().isOk())
                .andExpect(view().name("depliants/list"))
                .andExpect(model().attributeExists("depliants"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("depliants", hasItem(hasProperty("id", is(4L)))))
                .andExpect(model().attribute("depliants", hasItem(hasProperty("id", is(5L)))))
                .andExpect(model().attribute("depliants", hasItem(hasProperty("id", is(6L)))))
                .andExpect(model().attribute("depliants", hasSize(3)));
    }

    @Test
    @WithUserDetails("test1")
    void newDepliantForm_shouldReturnForm() throws Exception {

        mockMvc.perform(get("/depliants/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("depliants/form"))
                .andExpect(model().attributeExists("depliant"));
    }

    @Test
    @WithUserDetails("test1")
    void saveDepliant_shouldRedirect() throws Exception {

        mockMvc.perform(post("/depliants")
                        .param("number", "123")
                        .param("description", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/depliants"));
    }

    @Test
    @WithUserDetails("test1")
    void updateDepliantForm_shouldReturnForm() throws Exception {

        mockMvc.perform(get("/depliants/update/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("depliants/form"))
                .andExpect(model().attributeExists("depliant"));
    }

    @Test
    @WithUserDetails("test1")
    void deleteDepliant_shouldRedirect() throws Exception {

        mockMvc.perform(delete("/depliants/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/depliants"));
    }
}