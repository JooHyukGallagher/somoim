package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountRepository;
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

//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    SocietyService societyService;
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    SocietyRepository societyRepository;
//    @Autowired
//    SocietyManagerRepository societyManagerRepository;
//
//    @DisplayName("동호회 개설 폼 조회")
//    @WithAccount("joohyuk")
//    @Test
//    public void createSocietyForm() throws Exception {
//        mockMvc.perform(get("/new-society"))
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("societyForm"))
//                .andExpect(view().name("society/form"));
//    }
//
//    @DisplayName("동호회 개설 - 완료")
//    @WithAccount("joohyuk")
//    @Test
//    public void createSociety_success() throws Exception {
//        mockMvc.perform(post("/new-society")
//                .param("path", "test-path")
//                .param("title", "study title")
//                .param("shortDescription", "short description of a society")
//                .param("fullDescription", "full description of a society")
//                .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/society/test-path"));
//
//        Society society = societyRepository.findByPath("test-path").get();
//        assertThat(society).isNotNull();
//        SocietyManager societyManager = societyManagerRepository.findBySociety(society).get(0);
//        assertThat(societyManager).isNotNull();
//        Account manager = accountRepository.findByNickname("joohyuk").get();
//        assertThat(societyManager.getManager().getId()).isEqualTo(manager.getId());
//        assertThat(societyManager.getSociety().getId()).isEqualTo(society.getId());
//    }
//
//    @DisplayName("동호회 조회")
//    @WithAccount("joohyuk")
//    @Test
//    public void viewSociety() throws Exception {
//        Society society = Society.builder()
//                .path("test-path")
//                .title("test study")
//                .shortDescription("short description")
//                .fullDescription("full description")
//                .build();
//        Account joohyuk = accountRepository.findByNickname("joohyuk").get();
//        societyService.createNewSociety(society, joohyuk);
//
//        mockMvc.perform(get("/society/test-path"))
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("society"))
//                .andExpect(view().name("society/view"));
//    }

}