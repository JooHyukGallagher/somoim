package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountRepository;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyRepository;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class SocietyControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    SocietyService societyService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SocietyRepository societyRepository;

    @DisplayName("동호회 개설 폼 조회")
    @WithAccount("joohyuk")
    @Test
    public void createSocietyForm() throws Exception {
        mockMvc.perform(get("/new-society"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("societyForm"))
                .andExpect(view().name("society/form"));
    }

    @DisplayName("동호회 개설 - 완료")
    @WithAccount("joohyuk")
    @Test
    public void createSociety_success() throws Exception {
        mockMvc.perform(post("/new-society")
                .param("path", "test-path")
                .param("title", "study title")
                .param("shortDescription", "short description of a society")
                .param("fullDescription", "full description of a society")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/test-path"));

        Society society = societyService.getSociety("test-path");
        Account account = accountService.getAccount("joohyuk");

        assertThat(society).isNotNull();
        assertThat(society.getManagers().contains(account)).isTrue();
    }

    @DisplayName("동호회 개설 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void createSociety_fail() throws Exception {
        Society society = Society.builder()
                .path("test-path")
                .title("test society")
                .shortDescription("short description")
                .fullDescription("<p>full description</p>")
                .build();

        Account joohyuk = accountService.getAccount("joohyuk");
        societyService.createNewSociety(society, joohyuk);

        mockMvc.perform(post("/new-society")
                .param("path", "test-path")
                .param("title", "society title")
                .param("shortDescription", "short description of a society")
                .param("fullDescription", "full description of a society")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("society/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("societyForm"))
                .andExpect(model().attributeExists("account"));
    }

    @DisplayName("동호회 조회")
    @WithAccount("joohyuk")
    @Test
    public void viewSociety() throws Exception {
        Society society = Society.builder()
                .path("test-path")
                .title("test study")
                .shortDescription("short description")
                .fullDescription("<p>full description<p>")
                .build();

        Account joohyuk = accountService.getAccount("joohyuk");
        societyService.createNewSociety(society, joohyuk);

        mockMvc.perform(get("/society/test-path"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/view"));
    }

}