package me.weekbelt.runningflex.modules.event.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/society/{path}")
@Controller
public class EventController {

    private final SocietyService societyService;

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path,
                               Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        model.addAttribute("society", society);
        model.addAttribute("account", account);
        model.addAttribute("eventForm", new EventForm());
        return "event/form";
    }
}
