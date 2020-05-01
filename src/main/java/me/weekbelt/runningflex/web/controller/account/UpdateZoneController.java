package me.weekbelt.runningflex.web.controller.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.account.AccountService;
import me.weekbelt.runningflex.domain.account.CurrentAccount;
import me.weekbelt.runningflex.domain.zone.Zone;
import me.weekbelt.runningflex.domain.zone.ZoneRepository;
import me.weekbelt.runningflex.domain.zone.ZoneService;
import me.weekbelt.runningflex.web.dto.zone.ZoneForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class UpdateZoneController {

    private final ObjectMapper objectMapper;
    private final ZoneRepository zoneRepository;
    private final AccountService accountService;
    private final ZoneService zoneService;

    @GetMapping("/settings/zones")
    public String updateZonesForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute("account", account);

        List<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones", zones.stream().map(Zone::toString)
                .collect(Collectors.toList()));

        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString)
                .collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return "account/settings/zones";
    }

    @ResponseBody
    @PostMapping("/settings/zones/add")
    public ResponseEntity<?> addZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneService.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/settings/zones/remove")
    public ResponseEntity<?> removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneService.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }
}
