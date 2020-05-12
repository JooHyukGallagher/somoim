package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountFactory;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateSocietyDescriptionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountFactory accountFactory;
    @Autowired
    SocietyFactory societyFactory;
    @Autowired
    AccountService accountService;

    @DisplayName("소모임 소개 수정 폼 조회 - 실패 (권한 없는 유저)")
    @WithAccount("joohyuk")
    @Test
    public void updateDescriptionForm_fail() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test-study", weekbelt);

        mockMvc.perform(get("/society" + society.getPath() + "/settings/description"))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("소모임 소개 수정 폼 조회 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void updateDescriptionForm_success() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test-study", joohyuk);

        mockMvc.perform(get("/society/" + society.getPath() + "/settings/description"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("societyDescriptionForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/settings/description"));
    }

    @DisplayName("소모임 소개 수정 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void updateDescription_success() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test-study", joohyuk);

        mockMvc.perform(post("/society/" + society.getPath() + "/settings/description")
                .param("shortDescription", "short Description")
                .param("fullDescription", "full Description")
                .with(csrf()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/society/" + society.getPath() + "/settings/description"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("소모임 소개 수정 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void updateDescription_fail() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test-study", joohyuk);

        mockMvc.perform(post("/society/" + society.getPath() + "/settings/description")
                .param("shortDescription", "")
                .param("fullDescription", "full Description")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/settings/description"));
    }
}