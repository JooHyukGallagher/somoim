package me.weekbelt.runningflex.modules.enrollment.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.enrollment.EnrollmentService;
import me.weekbelt.runningflex.modules.event.Event;
import me.weekbelt.runningflex.modules.event.EventService;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/society/{path}")
@Controller
public class EnrollmentController {

    private final SocietyService societyService;
    private final EventService eventService;
    private final EnrollmentService enrollmentService;

    @PostMapping("/events/{id}/enroll")
    public String newEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                @PathVariable("id") Event event) {
        Society society = societyService.getSocietyToEnroll(path);
        eventService.newEnrollment(event, account);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    @PostMapping("/events/{id}/disenroll")
    public String cancelEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                   @PathVariable("id") Event event) {
        Society society = societyService.getSocietyToEnroll(path);
        eventService.cancelEnrollment(event, account);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    // 화면 칸 간격을 맞추기위해 Get요청을 보냄 원래 POST요청을 해야함
    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/accept")
    public String acceptEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                   @PathVariable("eventId") Event event,
                                   @PathVariable("enrollmentId") Enrollment enrollment) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        eventService.acceptEnrollment(event, enrollment);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/reject")
    public String rejectEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                   @PathVariable("eventId") Event event,
                                   @PathVariable("enrollmentId") Enrollment enrollment) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        eventService.rejectEnrollment(event, enrollment);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/checkin")
    public String checkInEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                    @PathVariable("eventId") Event event,
                                    @PathVariable("enrollmentId") Enrollment enrollment) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        eventService.checkInEnrollment(enrollment);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/cancel-checkin")
    public String cancelCheckInEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                          @PathVariable("eventId") Event event,
                                          @PathVariable("enrollmentId") Enrollment enrollment) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        eventService.cancelCheckInEnrollment(enrollment);
        return "redirect:/society/" + society.getEncodedPath() + "/events/" + event.getId();
    }
}
