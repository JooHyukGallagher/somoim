package me.weekbelt.runningflex.web.controller.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class UpdateTagController {

    private final TagService tagService;
    private final AccountService accountService;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;

    @GetMapping("/settings/tags")
    public String updateTags(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute("account", account);

        List<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle)
                .collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream()
                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return "account/settings/tags";
    }

    @ResponseBody
    @PostMapping("/settings/tags/add")
    public ResponseEntity<?> addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagService.findSavedTagByTitle(title);

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/settings/tags/remove")
    public ResponseEntity<?> removeTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagService.findTagByTitle(title);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }

}
