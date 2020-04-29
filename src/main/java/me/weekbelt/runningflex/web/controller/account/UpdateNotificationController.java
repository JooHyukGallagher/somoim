package me.weekbelt.runningflex.web.controller.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.account.AccountService;
import me.weekbelt.runningflex.domain.account.CurrentUser;
import me.weekbelt.runningflex.web.dto.account.Notifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UpdateNotificationController {

    private final AccountService accountService;

    @GetMapping("/settings/notifications")
    public String updateNotificationsForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("notifications", new Notifications(account));
        return "account/settings/notifications";
    }

    @PostMapping("/settings/notifications")
    public String updateNotifications(@CurrentUser Account account, @Valid Notifications notifications,
                                      Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "account/settings/notifications";

        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/settings/notifications";
    }
}
