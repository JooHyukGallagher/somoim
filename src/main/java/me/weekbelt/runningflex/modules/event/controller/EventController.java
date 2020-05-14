package me.weekbelt.runningflex.modules.event.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.event.Event;
import me.weekbelt.runningflex.modules.event.EventRepository;
import me.weekbelt.runningflex.modules.event.EventService;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.event.validator.EventValidator;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/society/{path}")
@Controller
public class EventController {

    private final SocietyService societyService;
    private final EventService eventService;
    private final EventValidator eventValidator;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

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

    @GetMapping("/events")
    public String viewSocietyEvents(@CurrentAccount Account account, @PathVariable String path,
                                    Model model) {
        Society society = societyService.getSocietyByPath(path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);

        List<Event> events = eventRepository.findBySocietyOrderByStartDateTime(society);
        List<Event> newEvents = new ArrayList<>();
        List<Event> oldEvents = new ArrayList<>();
        events.forEach(e -> {
            if (e.getEndDateTime().isBefore(LocalDateTime.now())) {
                oldEvents.add(e);
            } else {
                newEvents.add(e);
            }
        });
        model.addAttribute("newEvents", newEvents);
        model.addAttribute("oldEvents", oldEvents);

        return "society/events";
    }

    @GetMapping("/events/{id}/edit")
    public String updateEventForm(@CurrentAccount Account account, @PathVariable String path,
                                  @PathVariable Long id, Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        Event event = eventService.getEventById(id);

        model.addAttribute("account", account);
        model.addAttribute("society", society);
        model.addAttribute("event", event);
        model.addAttribute("eventForm", modelMapper.map(event, EventForm.class));
        return "event/update-form";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEventSubmit(@CurrentAccount Account account, @PathVariable String path,
                                    @PathVariable Long id, @Valid EventForm eventForm,
                                    Errors errors, Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        Event event = eventService.getEventById(id);
        // 화면에 EventType 변경 화면이 없어도 url에 요청이 가능하기때문에 방지
        eventForm.setEventType(event.getEventType());

        eventValidator.validateUpdateForm(eventForm, event, errors);
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("society", society);
            model.addAttribute("event", event);
            return "event/update-form";
        }

        eventService.updateEvent(event, eventForm);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    @DeleteMapping("/events/{id}")
    public String cancelEvent(@CurrentAccount Account account, @PathVariable String path,
                                         @PathVariable Long id) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdateStatus(account, path);
        eventService.deleteEvent(eventService.getEventById(id));
        return "redirect:/society/" + society.getEncodedPath() + "/events";
    }

    @PostMapping("/events/{id}/enroll")
    public String newEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                @PathVariable Long id) {
        Society society = societyService.getSocietyToEnroll(path);
        eventService.newEnrollment(eventService.getEventById(id), account);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + id;
    }

    @PostMapping("/events/{id}/disenroll")
    public String cancelEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                @PathVariable Long id) {
        Society society = societyService.getSocietyToEnroll(path);
        eventService.cancelEnrollment(eventService.getEventById(id), account);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + id;
    }

}
