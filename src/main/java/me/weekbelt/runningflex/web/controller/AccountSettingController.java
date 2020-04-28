package me.weekbelt.runningflex.web.controller;

import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.account.CurrentUser;
import me.weekbelt.runningflex.web.dto.account.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountSettingController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("profile", new Profile(account));
        return "account/settings/profile";
    }
}
