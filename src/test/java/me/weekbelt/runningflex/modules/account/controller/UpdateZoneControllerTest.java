package me.weekbelt.runningflex.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.zone.Zone;
import me.weekbelt.runningflex.modules.zone.ZoneRepository;
import me.weekbelt.runningflex.modules.zone.ZoneService;
import me.weekbelt.runningflex.modules.zone.form.ZoneForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateZoneControllerTest{

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountService accountService;
    @Autowired ZoneService zoneService;
    @Autowired ZoneRepository zoneRepository;

    private Zone testZone = Zone.builder()
            .city("test").localNameOfCity("테스트시").province("테스트주")
            .build();

    @BeforeEach
    void BeforeEach() {
        zoneRepository.save(testZone);
    }

    @WithAccount("joohyuk")
    @DisplayName("계정의 지역 정보 수정 폼")
    @Test
    public void updateZonesForm() throws Exception {
        mockMvc.perform(get("/settings/zones"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(view().name("account/settings/zones"));
    }

    @WithAccount("joohyuk")
    @DisplayName("계정의 지역 정보 추가")
    @Test
    public void addZone() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post("/settings/zones/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Account joohyuk = accountService.findByNickname("joohyuk");
        Zone zone = zoneService.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertThat(joohyuk.getZones().contains(zone)).isTrue();
    }

    @WithAccount("joohyuk")
    @DisplayName("계정의 지역 정보 삭제")
    @Test
    public void removeZone() throws Exception {
        Account joohyuk = accountService.findByNickname("joohyuk");
        Zone zone = zoneService.findByCityAndProvince(testZone.getCity(),
                testZone.getProvince());
        accountService.addZone(joohyuk, zone);

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post("/settings/zones/remove")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertThat(joohyuk.getZones().contains(zone)).isTrue();
    }
}