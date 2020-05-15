package me.weekbelt.runningflex.modules.enrollment.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountFactory;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.enrollment.EnrollmentRepository;
import me.weekbelt.runningflex.modules.event.*;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EnrollmentControllerTest {
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
    @Autowired
    EventService eventService;
    @Autowired
    EventFactory eventFactory;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithAccount("joohyuk")
    @Test
    public void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test-study", weekbelt);
        Event event = eventFactory.createEvent("test-event", EventType.FCFS, 2,
                society, weekbelt);

        String requestUrl = "/society/" + society.getEncodedPath() + "/events/" + event.getId() + "/enroll";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/events/" + event.getId()));

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        isAccepted(joohyuk, event);
    }

    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉차서)")
    @WithAccount("joohyuk")
    @Test
    public void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test-society", weekbelt);
        Event event = eventFactory.createEvent("test-event", EventType.FCFS, 2, society, weekbelt);

        Account twins = accountFactory.createAccount("twins");
        Account giants = accountFactory.createAccount("giants");
        eventService.newEnrollment(event, twins);
        eventService.newEnrollment(event, giants);

        String requestUrl = "/society/" + society.getEncodedPath() + "/events/" + event.getId() + "/enroll";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/events/" + event.getId()));

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        isNotAccepted(joohyuk, event);
    }

    @DisplayName("선착순 모임에 참가 신청 - 취소")
    @WithAccount("joohyuk")
    @Test
    public void cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Account twins = accountFactory.createAccount("twins");
        Society society = societyFactory.createSociety("test-study", weekbelt);
        Event event = eventFactory.createEvent("test-event", EventType.FCFS, 2, society, weekbelt);

        eventService.newEnrollment(event, twins);
        eventService.newEnrollment(event, joohyuk);
        eventService.newEnrollment(event, weekbelt);

        isAccepted(twins, event);
        isAccepted(joohyuk, event);
        isNotAccepted(weekbelt, event);

        String requestUrl = "/society/" + society.getEncodedPath() + "/events/" + event.getId() + "/disenroll";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/events/" + event.getId()));

        isAccepted(twins, event);
        isAccepted(weekbelt, event);
        assertThat(enrollmentRepository.findByEventAndAccount(event, joohyuk).orElse(null)).isNull();
    }

    @DisplayName("관리자 확인 모임에 참가 신청 - 대기중")
    @WithAccount("joohyuk")
    @Test
    public void newEnrollment_to_CONFIRMATIVE_event_not_accepted() throws Exception {
        Account weekbelt = accountFactory.createAccount("weekbelt");
        Society society = societyFactory.createSociety("test-study", weekbelt);
        Event event = eventFactory.createEvent("test-event", EventType.CONFIRMATIVE, 2, society, weekbelt);

        String requestUrl = "/society/" + society.getEncodedPath() + "/events/" + event.getId() + "/enroll";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/events/" + event.getId()));

        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        isNotAccepted(joohyuk, event);
    }

    private void isAccepted(Account joohyuk, Event event) {
        assertThat(enrollmentRepository.findByEventAndAccount(event, joohyuk)
                .orElse(null).isAccepted()).isTrue();

    }

    private void isNotAccepted(Account joohyuk, Event event) {
        assertThat(enrollmentRepository.findByEventAndAccount(event, joohyuk)
                .orElse(null).isAccepted()).isFalse();
    }
}