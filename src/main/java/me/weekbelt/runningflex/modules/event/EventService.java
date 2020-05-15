package me.weekbelt.runningflex.modules.event;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EnrollmentRepository enrollmentRepository;

    public Event createEvent(Event event, Society society, Account account) {
        event.createEvent(society, account);
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow((() -> new IllegalArgumentException("찾는 모임이 없습니다.")));
    }

    public void updateEvent(Event event, EventForm eventForm) {
        event.updateEvent(eventForm);
        event.acceptWaitingList();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public void newEnrollment(Event event, Account account) {
        if (!enrollmentRepository.existsByEventAndAccount(event, account)){
            Enrollment enrollment = Enrollment.builder()
                    .enrolledAt(LocalDateTime.now())
                    .accepted(event.isAbleToAcceptWaitingEnrollment())
                    .account(account)
                    .build();
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account)
                .orElseThrow(() -> new IllegalArgumentException("찾는 모임 신청자가 없습니다."));
        if (!enrollment.isAttended()){
            event.removeEnrollment(enrollment);
            enrollmentRepository.delete(enrollment);
            event.acceptNextWaitingEnrollment();
        }
    }

    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.attended();
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.notAttended();
    }
}
