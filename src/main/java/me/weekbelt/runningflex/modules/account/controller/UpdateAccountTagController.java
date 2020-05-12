package me.weekbelt.runningflex.modules.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.tag.TagRepository;
import me.weekbelt.runningflex.modules.tag.TagService;
import me.weekbelt.runningflex.modules.tag.form.TagForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class UpdateAccountTagController {

    private final TagService tagService;
    private final AccountService accountService;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;

    @GetMapping("/settings/tags")
    public String updateTags(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute("account", account);

        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle)
                .collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream()
                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return "account/settings/tags";
    }

    @ResponseBody
    @PostMapping("/settings/tags/add")
    public ResponseEntity<?> addTag(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagService.findOrCreateNew(title);

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/settings/tags/remove")
    public ResponseEntity<?> removeTag(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagService.findTagByTitle(title);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }

}
