package me.weekbelt.runningflex.modules.event.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.event.Event;
import me.weekbelt.runningflex.modules.event.EventService;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.event.validator.EventValidator;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/society/{path}")
@Controller
public class EventController {

    private final SocietyService societyService;
    private final EventService eventService;
    private final EventValidator eventValidator;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventValidator);
    }

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path,
                               Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        model.addAttribute("society", society);
        model.addAttribute("account", account);
        model.addAttribute("eventForm", new EventForm());
        return "event/form";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@CurrentAccount Account account, @PathVariable String path,
                                 @Valid EventForm eventForm, Errors errors, Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("society", society);
            return "event/form";
        }

        Event event = Event.builder()
                .title(eventForm.getTitle())
                .description(eventForm.getDescription())
                .eventType(eventForm.getEventType())
                .endEnrollmentDateTime(eventForm.getEndEnrollmentDateTime())
                .startDateTime(eventForm.getStartDateTime())
                .endDateTime(eventForm.getEndDateTime())
                .limitOfEnrollments(eventForm.getLimitOfEnrollments())
                .build();

        Event createdEvent = eventService.createEvent(event, society, account);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + createdEvent.getId();
    }

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentAccount Account account, @PathVariable String path,
                           @PathVariable Long id, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("society", societyService.getSocietyByPath(path));
        return "event/view";
    }
}
