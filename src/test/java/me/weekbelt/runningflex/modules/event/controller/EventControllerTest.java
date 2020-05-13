package me.weekbelt.runningflex.modules.event.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountFactory;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.event.Event;
import me.weekbelt.runningflex.modules.event.EventType;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @DisplayName("모임 생성 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void newEvent_Success() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        String title = "test title";
        String description = "test description";
        String eventType = EventType.FCFS.toString();
        LocalDateTime now = LocalDateTime.now();
        String endEnrollmentDateTime = now.plusDays(1).toString();
        String startDateTime = now.plusDays(2).toString();
        String endDateTime = now.plusHours(5).toString();

        String requestUrl = "/society/" + society.getEncodedPath() + "/new-event";
        mockMvc.perform(post(requestUrl)
                .param("title", title)
                .param("description", description)
                .param("eventType", eventType)
                .param("endEnrollmentDateTime", endEnrollmentDateTime)
                .param("startDateTime", startDateTime)
                .param("endDateTime", endDateTime)
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("모임 생성 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void newEvent_Fail() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        String title = "test title";
        String description = "test description";
        String eventType = EventType.FCFS.toString();
        LocalDateTime now = LocalDateTime.now();
        String endEnrollmentDateTime = now.plusDays(3).toString();
        String startDateTime = now.plusDays(2).toString();
        String endDateTime = now.plusHours(5).toString();

        String requestUrl = "/society/" + society.getEncodedPath() + "/new-event";
        mockMvc.perform(post(requestUrl)
                .param("title", title)
                .param("description", description)
                .param("eventType", eventType)
                .param("endEnrollmentDateTime", endEnrollmentDateTime)
                .param("startDateTime", startDateTime)
                .param("endDateTime", endDateTime)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("event/form"))
                .andExpect(model().hasErrors());
    }

}