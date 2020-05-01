package me.weekbelt.runningflex.web.controller.account;

import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.accountZone.AccountZone;
import me.weekbelt.runningflex.domain.zone.Zone;
import me.weekbelt.runningflex.web.BasicControllerTest;
import me.weekbelt.runningflex.web.controller.WithAccount;
import me.weekbelt.runningflex.web.dto.zone.ZoneForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdateZoneControllerTest extends BasicControllerTest {

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

        Account account = accountService.findByNickname("joohyuk");
        List<AccountZone> accountZones = accountZoneRepository.findByAccountId(account.getId());
        AccountZone accountZone = accountZones.get(0);

        assertThat(accountZone.getAccount().getId()).isEqualTo(account.getId());
        assertThat(accountZone.getZone().getCity()).isEqualTo(testZone.getCity());
        assertThat(accountZone.getZone().getLocalNameOfCity()).isEqualTo(testZone.getLocalNameOfCity());
        assertThat(accountZone.getZone().getProvince()).isEqualTo(testZone.getProvince());
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

        List<Zone> zones = accountService.getZones(joohyuk);
        assertThat(zones.size()).isZero();
    }
}