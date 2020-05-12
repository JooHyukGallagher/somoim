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
public class UpdateSocietyBannerController {

    private final SocietyService societyService;

    @GetMapping("/banner")
    public String SocietyImageForm(@CurrentAccount Account account,
                                   @PathVariable String path, Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);
        return "society/settings/banner";
    }

    @PostMapping("/banner")
    public String studyImageSubmit(@CurrentAccount Account account, @PathVariable String path,
                                   String image, RedirectAttributes attributes) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        societyService.updateSocietyImage(society, image);
        attributes.addFlashAttribute("message", "스터디 이미지를 수정했습니다.");
        return "redirect:/society/" + society.getEncodedPath() + "/settings/banner";
    }

    @PostMapping("/banner/enable")
    public String enableStudyBanner(@CurrentAccount Account account, @PathVariable String path) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        societyService.enableSocietyBanner(society);
        return "redirect:/society/" + society.getEncodedPath() + "/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableStudyBanner(@CurrentAccount Account account, @PathVariable String path) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        societyService.disableSocietyBanner(society);
        return "redirect:/society/" + society.getEncodedPath() + "/settings/banner";
    }
}
