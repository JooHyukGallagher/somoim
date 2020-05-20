package me.weekbelt.runningflex.modules.society.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.service.SocietyService;
import me.weekbelt.runningflex.modules.zone.Zone;
import me.weekbelt.runningflex.modules.zone.ZoneRepository;
import me.weekbelt.runningflex.modules.zone.form.ZoneForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/society/{path}/settings")
@Controller
public class UpdateSocietyZoneController {

    private final SocietyService societyService;
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/zones")
    public String societyZonesForm(@CurrentAccount Account account, @PathVariable String path,
                                   Model model) throws AccessDeniedException, JsonProcessingException {
        Society society = societyService.getSocietyToUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);
        List<String> zones = society.getZones().stream()
                .map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("zones", zones);
        List<String> whitelist = zoneRepository.findAll().stream()
                .map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));

        return "society/settings/zones";
    }

    @ResponseBody
    @PostMapping("/zones/add")
    public ResponseEntity<?> addZone(@CurrentAccount Account account, @PathVariable String path,
                                     @RequestBody ZoneForm zoneForm) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName())
                .orElse(null);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        societyService.addZone(society, zone);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/zones/remove")
    public ResponseEntity<?> removeZone(@CurrentAccount Account account, @PathVariable String path,
                                     @RequestBody ZoneForm zoneForm) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName())
                .orElse(null);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        societyService.removeZone(society, zone);
        return ResponseEntity.ok().build();
    }
}
