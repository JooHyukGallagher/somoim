package me.weekbelt.runningflex.modules.main;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.repository.SocietyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final SocietyRepository societyRepository;

    @GetMapping("/")
    public String mainPage(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/society")
    public String searchSociety(String keyword, Model model) {
        List<Society> societyList = societyRepository.findByKeyword(keyword);
        model.addAttribute("societyList", societyList);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
