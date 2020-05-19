package me.weekbelt.runningflex.modules.main;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.repository.SocietyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String searchSociety(String keyword, Model model,
                                @PageableDefault(size = 9, sort = "publishedDateTime",
                                        direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Society> societyPage = societyRepository.findByKeyword(keyword, pageable);
        model.addAttribute("societyPage", societyPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty", pageable.getSort()
                .toString().contains("publishedDateTime") ? "publishedDateTime" : "memberCount");
        return "search";
    }
}
