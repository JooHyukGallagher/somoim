package me.weekbelt.runningflex.web.controller.account;

import me.weekbelt.runningflex.web.BasicControllerTest;
import me.weekbelt.runningflex.web.controller.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UpdateZoneControllerTest extends BasicControllerTest {

    @DisplayName("지역 정보 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateZonesForm() throws Exception {
        mockMvc.perform(get("/settings/zones"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(view().name("account/settings/zones"));
    }
}