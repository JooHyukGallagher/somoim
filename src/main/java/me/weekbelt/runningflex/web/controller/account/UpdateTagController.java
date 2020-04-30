package me.weekbelt.runningflex.web.controller.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.account.AccountService;
import me.weekbelt.runningflex.domain.account.CurrentUser;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.tag.TagRepository;
import me.weekbelt.runningflex.domain.tag.TagService;
import me.weekbelt.runningflex.web.dto.tag.TagForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class UpdateTagController {

    private final TagService tagService;
    private final AccountService accountService;

    @GetMapping("/settings/tags")
    public String updateTags(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        return "account/settings/tags";
    }

    @ResponseBody
    @PostMapping("/settings/tags/add")
    public ResponseEntity<?> addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagService.findByTitle(title);

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }
}
