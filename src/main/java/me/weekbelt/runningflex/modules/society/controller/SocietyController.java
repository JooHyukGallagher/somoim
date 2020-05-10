package me.weekbelt.runningflex.modules.society.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import me.weekbelt.runningflex.modules.society.form.SocietyForm;
import me.weekbelt.runningflex.modules.society.validator.SocietyFormValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
public class SocietyController {

    private final SocietyService societyService;
    private final SocietyFormValidator societyFormValidator;

    @InitBinder("societyForm")
    public void societyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(societyFormValidator);
    }

    @GetMapping("/new-society")
    public String newSocietyForm(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("societyForm", new SocietyForm());
        return "society/form";
    }

    @PostMapping("/new-society")
    public String newSocietySubmit(@CurrentAccount Account account,
                                   @Valid SocietyForm societyForm, Errors errors,
                                   Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "society/form";
        }

        Society newSociety = Society.builder()
                .path(societyForm.getPath())
                .title(societyForm.getTitle())
                .shortDescription(societyForm.getShortDescription())
                .fullDescription(societyForm.getFullDescription())
                .build();

        Society society = societyService.createNewSociety(newSociety, account);
        return "redirect:/society/" + society.getEncodedPath();
    }

    @GetMapping("/society/{path}")
    public String viewSociety(@CurrentAccount Account account, @PathVariable String path,
                              Model model) {
        model.addAttribute("account", account);
        model.addAttribute("society", societyService.findSocietyByPath(path));
        return "society/view";
    }

}
