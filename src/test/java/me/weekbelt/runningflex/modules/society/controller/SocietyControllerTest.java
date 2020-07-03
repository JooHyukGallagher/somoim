package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.*;
import me.weekbelt.runningflex.modules.account.repository.AccountRepository;
import me.weekbelt.runningflex.modules.account.service.AccountService;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyType;
import me.weekbelt.runningflex.modules.society.repository.SocietyRepository;
import me.weekbelt.runningflex.modules.society.service.SocietyService;
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
    @Autowired
    SocietyFactory societyFactory;
    @Autowired
    AccountFactory accountFactory;

    @DisplayName("동호회 개설 폼 조회 성공 - 이메일 인증을 받은 경우")
    @WithAccount("joohyuk")
    @Test
    public void createSocietyForm_success() throws Exception {
        accountRepository.findByNickname("joohyuk")
                .ifPresent(account -> accountService.completeSignUp(account));

        mockMvc.perform(get("/new-society"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("societyForm"))
                .andExpect(view().name("society/form"));
    }

    @DisplayName("동호회 개설 폼 조회 실패 - 이메일 인증을 받지 않은 경우")
    @WithAccount("joohyuk")
    @Test
    public void createSocietyForm_fail() throws Exception {
        mockMvc.perform(get("/new-society"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/check-email"));
    }

    @DisplayName("동호회 개설 - 완료")
    @WithAccount("joohyuk")
    @Test
    public void createSociety_success() throws Exception {
        accountRepository.findByNickname("joohyuk")
                .ifPresent(account -> accountService.completeSignUp(account));

        mockMvc.perform(post("/new-society")
                .param("path", "test-path")
                .param("title", "study title")
                .param("shortDescription", "short description of a society")
                .param("fullDescription", "full description of a society")
                .param("societyType", SocietyType.FREE.toString())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/test-path"));

        Society society = societyService.getSocietyByPath("test-path");
        Account account = accountService.getAccountByNickname("joohyuk");

        assertThat(society).isNotNull();
        assertThat(society.getManagers().contains(account)).isTrue();
    }

    @DisplayName("동호회 개설 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void createSociety_fail() throws Exception {
        accountRepository.findByNickname("joohyuk")
                .ifPresent(account -> accountService.completeSignUp(account));

        Society society = Society.builder()
                .path("test-path")
                .title("test society")
                .shortDescription("short description")
                .fullDescription("<p>full description</p>")
                .societyType(SocietyType.FREE)
                .build();

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        societyService.createNewSociety(society, joohyuk);

        mockMvc.perform(post("/new-society")
                .param("path", "test-path")
                .param("title", "society title")
                .param("shortDescription", "short description of a society")
                .param("fullDescription", "full description of a society")
                .param("societyType", SocietyType.FREE.toString())
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

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        societyService.createNewSociety(society, joohyuk);

        mockMvc.perform(get("/society/test-path"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/view"));
    }

    @DisplayName("소모임 가입")
    @WithAccount("joohyuk")
    @Test
    public void joinSociety() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test", weekbelt);

        societyService.publish(society);
        societyService.startRecruit(society);

        String requestUrl = "/society/" + society.getEncodedPath() + "/join";
        mockMvc.perform(get(requestUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/members"));

        Society findSociety = societyRepository.findSocietyWithMembersByPath("test").orElse(null);
        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        assertThat(findSociety.getMembers().contains(joohyuk)).isTrue();
    }

    @DisplayName("소모임 탈퇴")
    @WithAccount("joohyuk")
    @Test
    public void leaveSociety() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test", weekbelt);

        societyService.publish(society);
        societyService.startRecruit(society);

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        societyService.addMembers(society, joohyuk);

        String requestUrl = "/society/" + society.getEncodedPath() + "/leave";
        mockMvc.perform(get(requestUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/members"));

        Society findSociety = societyRepository.findSocietyWithMembersByPath("test").orElse(null);
        assertThat(findSociety.getMembers().contains(joohyuk)).isFalse();
    }
}