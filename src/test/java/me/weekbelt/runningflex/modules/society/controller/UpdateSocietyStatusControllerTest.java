package me.weekbelt.runningflex.modules.society.controller;

import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateSocietyStatusControllerTest {

    @Autowired
    SocietyFactory societyFactory;
    @Autowired
    AccountService accountService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    SocietyService societyService;

    @DisplayName("스터디 상태페이지")
    @WithAccount("joohyuk")
    @Test
    public void societyStatusForm() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/society";
        mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(view().name("society/settings/society"));
    }

    @DisplayName("소모임 공개")
    @WithAccount("joohyuk")
    @Test
    public void publishSociety() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        assertThat(society.isPublished()).isFalse();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isFalse();

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/society/publish";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/settings/society"));

        Society findSociety = societyService.getSocietyToUpdateStatus(joohyuk, society.getEncodedPath());

        assertThat(findSociety.isPublished()).isTrue();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isFalse();
    }

    @DisplayName("소모임 닫기")
    @WithAccount("joohyuk")
    @Test
    public void closeSociety() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        societyService.publish(society);

        assertThat(society.isPublished()).isTrue();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isFalse();

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/society/close";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/settings/society"));

        Society findSociety = societyService.getSocietyToUpdateStatus(joohyuk, society.getEncodedPath());

        assertThat(findSociety.isPublished()).isTrue();
        assertThat(society.isClosed()).isTrue();
        assertThat(society.isRecruiting()).isFalse();
    }

    @DisplayName("소모임 인원 모집 시작")
    @WithAccount("joohyuk")
    @Test
    public void startRecruit_Success() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        societyService.publish(society);

        assertThat(society.isPublished()).isTrue();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isFalse();

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/recruit/start";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(flash().attribute("message", "인원 모집을 시작합니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/settings/society"));

        Society findSociety = societyService.getSocietyToUpdateStatus(joohyuk, society.getEncodedPath());

        assertThat(findSociety.isPublished()).isTrue();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isTrue();
    }

    @DisplayName("소모임 인원 모집 시작 실패")
    @WithAccount("joohyuk")
    @Test
    public void startRecruit_Fail() throws Exception {
        Account joohyuk = accountService.getAccount("joohyuk");
        Society society = societyFactory.createSociety("test", joohyuk);

        societyService.publish(society);

        assertThat(society.isPublished()).isTrue();
        assertThat(society.isClosed()).isFalse();
        assertThat(society.isRecruiting()).isFalse();

        societyService.startRecruit(society); // 모집시작

        // 바로 또 모집 시작하는경우 실패
        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/recruit/start";
        mockMvc.perform(post(requestUrl)
                .with(csrf()))
                .andExpect(flash().attribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/society/" + society.getEncodedPath() + "/settings/society"));
    }


}
