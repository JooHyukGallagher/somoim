package me.weekbelt.runningflex.modules.main;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.account.repository.AccountRepository;
import me.weekbelt.runningflex.modules.enrollment.repository.EnrollmentRepository;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyType;
import me.weekbelt.runningflex.modules.society.repository.SocietyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final SocietyRepository societyRepository;
    private final AccountRepository accountRepository;
    private final EnrollmentRepository enrollmentRepository;

    @GetMapping("/")
    public String mainPage(@CurrentAccount Account account, Model model) {
        if (account != null) {
            Account findAccount = accountRepository.findAccountWithTagsAndZonesById(account.getId());
            model.addAttribute("account", findAccount);
            model.addAttribute("enrollmentList",
                    enrollmentRepository.findByAccountAndAcceptedOrderByEnrolledAtDesc(account, true));
            model.addAttribute("societyList",
                    societyRepository.findByAccount(findAccount.getTags(), findAccount.getZones()));
            model.addAttribute("societyManagerOf",
                    societyRepository.findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(account, false));
            model.addAttribute("societyMemberOf",
                    societyRepository.findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(account, false));
            return "index-after-login";
        }

        List<Society> societyList = societyRepository
                .findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(true, false);
        model.addAttribute("societyList", societyList);
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

    @GetMapping("/search/society/category")
    public String searchSocietyByCategory(SocietyType societyType, Model model,
                                          @PageableDefault(size = 9, sort = "publishedDateTime",
                                                  direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Society> societyPage = societyRepository.findBySocietyType(societyType, pageable);
        model.addAttribute("societyPage", societyPage);
        model.addAttribute("societyType", societyType.getValue());
        model.addAttribute("sortProperty", pageable.getSort()
                .toString().contains("publishedDateTime") ? "publishedDateTime" : "memberCount");
        return "searchByCategory";
    }
}
