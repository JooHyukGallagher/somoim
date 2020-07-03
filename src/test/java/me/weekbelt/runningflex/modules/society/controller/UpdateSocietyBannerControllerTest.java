package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.account.repository.AccountRepository;
import me.weekbelt.runningflex.modules.account.service.AccountService;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.service.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateSocietyBannerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SocietyService societyService;
    @Autowired
    private SocietyFactory societyFactory;

    @DisplayName("배너 설정 폼 조회 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void settingBannerForm_Success() throws Exception {
        accountRepository.findByNickname("joohyuk")
                .ifPresent(account -> accountService.completeSignUp(account));

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        societyFactory.createSociety("test-path", joohyuk);

        mockMvc.perform(get("/society/test-path/settings/banner")
                .param("path", "/test-path"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/settings/banner"));
    }


//    @DisplayName("배너 설정 처리 - 성공")
//    @WithAccount("joohyuk")
//    @Test
//    public void settingBanner_Success() throws Exception {
//        accountRepository.findByNickname("joohyuk")
//                .ifPresent(account -> accountService.completeSignUp(account));
//
//        Account joohyuk = accountService.getAccountByNickname("joohyuk");
//        societyFactory.createSociety("test-path", joohyuk);
//
//        mockMvc.perform(post("/society/test-path/settings/banner"))
//    }

}