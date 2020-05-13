package me.weekbelt.runningflex.modules.event.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountFactory;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class EventControllerTest {

    @Autowired
    SocietyFactory societyFactory;
    @Autowired
    AccountFactory accountFactory;
    @Autowired
    SocietyService societyService;
    @Autowired
    AccountService accountService;
    @Autowired
    MockMvc mockMvc;

    @DisplayName("모임 생성 폼")
    @WithAccount("joohyuk")
    @Test
    public void newEventForm() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        String requestUrl = "/society/" + society.getEncodedPath() + "/new-event";
        mockMvc.perform(get(requestUrl))
                .andExpect(model().attributeExists("society"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("eventForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("event/form"));
    }
}