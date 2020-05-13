package me.weekbelt.runningflex.modules.society.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/society/{path}/settings")
@Controller
public class UpdateSocietyStatusController {

    private final SocietyService societyService;

    @GetMapping("/society")
    public String societySettingForm(@CurrentAccount Account account, @PathVariable String path,
                                     Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);
        return "society/settings/society";
    }

    @PostMapping("/society/publish")
    public String publishSociety(@CurrentAccount Account account, @PathVariable String path,
                                 RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        societyService.publish(society);
        attributes.addFlashAttribute("message", "소모임을 공개했습니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }

    @PostMapping("/society/close")
    public String closeSociety(@CurrentAccount Account account, @PathVariable String path,
                                 RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        societyService.close(society);
        attributes.addFlashAttribute("message", "소모임을 종료했습니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }
}
