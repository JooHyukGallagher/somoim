package me.weekbelt.runningflex.modules.society.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.service.SocietyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/recruit/start")
    public String startRecruit(@CurrentAccount Account account, @PathVariable String path,
                               RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        if (!society.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
        }

        societyService.startRecruit(society);
        attributes.addFlashAttribute("message", "인원 모집을 시작합니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }

    @PostMapping("/recruit/stop")
    public String stopRecruit(@CurrentAccount Account account, @PathVariable String path,
                              RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        if (!society.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
        }

        societyService.stopRecruit(society);
        attributes.addFlashAttribute("message", "인원 모집을 종료합니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }

    @PostMapping("/society/path")
    public String updateSocietyPath(@CurrentAccount Account account, @PathVariable String path,
                                    @RequestParam String newPath, Model model,
                                    RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        if (!societyService.isValidPath(newPath)) {
            model.addAttribute("account", account);
            model.addAttribute("society", society);
            model.addAttribute("societyPathError", "해당 소모임 경로는 사용할 수 없습니다. 다른 값을 입력하세요.");
            return "society/settings/society";
        }

        societyService.updateSocietyPath(society, newPath);
        attributes.addFlashAttribute("message", "소모임 경로를 수정했습니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }

    @PostMapping("/society/title")
    public String updateSocietyTitle(@CurrentAccount Account account, @PathVariable String path,
                                     @RequestParam String newTitle, Model model,
                                     RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        if (!societyService.isValidTitle(newTitle)) {
            model.addAttribute("account", account);
            model.addAttribute("society", society);
            model.addAttribute("societyTitleError", "소모임 이름을 다시 입력하세요.");
            return "society/settings/society";
        }

        societyService.updateSocietyTitle(society, newTitle);
        attributes.addFlashAttribute("message", "소모임 이름을 수정했습니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/society";
    }

    @PostMapping("/society/remove")
    public String removeSociety(@CurrentAccount Account account, @PathVariable String path)
            throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        societyService.remove(society);
        return "redirect:/";
    }
}
