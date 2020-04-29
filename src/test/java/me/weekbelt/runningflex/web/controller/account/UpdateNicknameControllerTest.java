package me.weekbelt.runningflex.web.controller.account;

import me.weekbelt.runningflex.web.BasicControllerTest;
import me.weekbelt.runningflex.web.controller.WithAccount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdateNicknameControllerTest extends BasicControllerTest {

    @WithAccount("joohyuk")
    @DisplayName("닉네임 수정 폼")
    @Test
    public void updateAccountForm() throws Exception {
        mockMvc.perform(get("/settings/account"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount("joohyuk")
    @DisplayName("닉네임 수정하기 - 입력값 정상")
    @Test
    public void updateAccount_success() throws Exception {
        String newNickname = "weekbelt";
        mockMvc.perform(post("/settings/account")
                .param("nickname", newNickname)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("message"));

        assertThat(accountRepository.findByNickname(newNickname).orElse(null)).isNotNull();
    }

    @WithAccount("joohyuk")
    @DisplayName("닉네임 수정하기 - 입력값 오류 - 중복 아이디 저장")
    @Test
    public void updateAccount_fail() throws Exception {
        String newNickname = "joohyuk";
        mockMvc.perform(post("/settings/account")
                .param("nickname", newNickname)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/account"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }
}