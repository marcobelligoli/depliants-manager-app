package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.config.ITContainersExtension;
import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.belligolifoundation.depliantsmanager.repository.DepliantRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Autowired
    private DepliantRepository depliantRepository;

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
    @Transactional
    @WithUserDetails("test1")
    void saveDepliant_shouldRedirect() throws Exception {

        mockMvc.perform(post("/depliants")
                        .with(csrf())
                        .param("number", "123")
                        .param("description", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/depliants"));
    }

    @Test
    @Transactional
    @WithUserDetails("test1")
    void saveDepliant_withForeignId_shouldNotOverwriteOtherUsersDepliant() throws Exception {

        // depliant 4 belongs to test2 (see sql/data.sql)
        mockMvc.perform(post("/depliants")
                        .with(csrf())
                        .param("id", "4")
                        .param("number", "999")
                        .param("description", "pwned"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/depliants"));

        Depliant victim = depliantRepository.findById(4L).orElseThrow();
        assertEquals("Cronaca movimento terra Fiat FR12", victim.getDescription());
        assertEquals(1, victim.getNumber());
        assertEquals(2L, victim.getUser().getId());
    }

    @Test
    @WithUserDetails("test1")
    void saveDepliant_withoutCsrfToken_shouldBeForbidden() throws Exception {

        mockMvc.perform(post("/depliants")
                        .param("number", "124")
                        .param("description", "forged"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("test1")
    void deleteDepliant_viaHiddenMethodWithoutCsrfToken_shouldBeForbidden() throws Exception {

        mockMvc.perform(post("/depliants/2")
                        .param("_method", "delete"))
                .andExpect(status().isForbidden());

        assertTrue(depliantRepository.existsById(2L));
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

        mockMvc.perform(delete("/depliants/7").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/depliants"));
    }
}