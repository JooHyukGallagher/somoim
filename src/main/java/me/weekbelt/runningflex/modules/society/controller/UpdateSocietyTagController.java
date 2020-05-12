package me.weekbelt.runningflex.modules.society.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.tag.TagRepository;
import me.weekbelt.runningflex.modules.tag.TagService;
import me.weekbelt.runningflex.modules.tag.form.TagForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/society/{path}/settings")
@Controller
public class UpdateSocietyTagController {

    private final SocietyService societyService;
    private final TagService tagService;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;

    @GetMapping("/tags")
    public String societyTagsForm(@CurrentAccount Account account, @PathVariable String path,
                                  Model model) throws AccessDeniedException, JsonProcessingException {
        Society society = societyService.getSocietyToUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);

        List<String> tags = society.getTags().stream()
                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("tags", tags);
        List<String> whitelist = tagRepository.findAll().stream()
                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));

        return "society/settings/tags";
    }

    @ResponseBody
    @PostMapping("/tags/add")
    public ResponseEntity<?> addTag(@CurrentAccount Account account, @PathVariable String path,
                                    @RequestBody TagForm tagForm) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateTag(account, path);
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        societyService.addTag(society, tag);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/tags/remove")
    public ResponseEntity<?> removeTag(@CurrentAccount Account account, @PathVariable String path,
                                    @RequestBody TagForm tagForm) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateTag(account, path);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle()).orElse(null);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        societyService.removeTag(society, tag);
        return ResponseEntity.ok().build();
    }
}
