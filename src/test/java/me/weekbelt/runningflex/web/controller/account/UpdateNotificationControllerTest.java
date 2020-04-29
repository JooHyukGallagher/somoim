package me.weekbelt.runningflex.web.controller.account;

import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.web.BasicControllerTest;
import me.weekbelt.runningflex.web.controller.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdateNotificationControllerTest extends BasicControllerTest {

    @WithAccount("joohyuk")
    @DisplayName("알림설정 수정 폼")
    @Test
    public void updateNotificationsForm() throws Exception {
        mockMvc.perform(get("/settings/notifications"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notifications"))
                .andExpect(view().name("account/settings/notifications"));
    }

    @WithAccount("joohyuk")
    @DisplayName("알림설정 수정 - 입력값 정상")
    @Test
    public void updateNotifications_success() throws Exception {
        mockMvc.perform(post("/settings/notifications")
                .param("groupCreatedByEmail", "true")
                .param("groupEnrollmentResultByEmail", "true")
                .param("groupUpdatedByEmail", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/notifications"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("joohyuk").get();
        assertThat(account.isGroupCreatedByEmail()).isTrue();
        assertThat(account.isGroupEnrollmentResultByEmail()).isTrue();
        assertThat(account.isGroupUpdatedByEmail()).isTrue();
    }

    @WithAccount("joohyuk")
    @DisplayName("알림설정 수정 - 입력값 오류")
    @Test
    public void updateNotifications_fail() throws Exception {
        mockMvc.perform(post("/settings/notifications")
                .param("groupCreatedByEmail", "123")
                .param("groupEnrollmentResultByEmail", "123")
                .param("groupUpdatedByEmail", "123")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/notifications"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("notifications"))
                .andExpect(model().attributeExists("account"))
        ;
    }
}