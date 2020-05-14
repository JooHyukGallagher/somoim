package me.weekbelt.runningflex.modules.society.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
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

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateSocietyZoneControllerTest {

    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    SocietyFactory societyFactory;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ZoneService zoneService;
    @Autowired
    SocietyService societyService;

    public Zone testZone = Zone.builder()
            .city("test").localNameOfCity("테스트시").province("테스트주")
            .build();

    @BeforeEach
    void beforeEach() {
        zoneRepository.save(testZone);
    }

    @DisplayName("소모임의 지역 정보 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateZoneForm() throws Exception {
        Society society = getSociety();

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/zones";
        mockMvc.perform(get(requestUrl))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(status().isOk())
                .andExpect(view().name("society/settings/zones"));
    }


    @DisplayName("소모임의 지역 정보 추가")
    @WithAccount("joohyuk")
    @Test
    public void addZone() throws Exception {
        Society society = getSociety();

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/zones/add";
        mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("소모임의 지역 정보 삭제")
    @WithAccount("joohyuk")
    @Test
    public void removeZone() throws Exception {
        Society society = getSociety();

        Zone zone = zoneService.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        societyService.addZone(society, zone);

        assertThat(society.getZones().contains(zone)).isTrue();

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/zones/remove";
        mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertThat(society.getZones().contains(zone)).isFalse();
    }

    private Society getSociety() {
        Account joohyuk = accountService.getAccountByNickname("joohyuk");
        return societyFactory.createSociety("test", joohyuk);
    }
}
