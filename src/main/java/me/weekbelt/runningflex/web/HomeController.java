package me.weekbelt.runningflex.web;

import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.account.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mainPage(@CurrentUser Account account, Model model){
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }
}
