package me.weekbelt.runningflex.modules.event;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.enrollment.EnrollmentRepository;
import me.weekbelt.runningflex.modules.event.event.EnrollmentAcceptedEvent;
import me.weekbelt.runningflex.modules.event.event.EnrollmentRejectEvent;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.event.SocietyUpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Event createEvent(Event event, Society society, Account account) {
        event.createEvent(society, account);
        eventPublisher.publishEvent(new SocietyUpdateEvent(event.getSociety(),
                "'" + event.getTitle() + "' 모임을 만들었습니다."));
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        event.updateEvent(eventForm);
        event.acceptWaitingList();
        eventPublisher.publishEvent(new SocietyUpdateEvent(event.getSociety(),
                "'" + event.getTitle() + "' 모임을 정보를 수정했으니 확인하세요."));
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        eventPublisher.publishEvent(new SocietyUpdateEvent(event.getSociety(),
                "'" + event.getTitle() + "' 모임을 취소했습니다."));
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
        eventPublisher.publishEvent(new EnrollmentAcceptedEvent(enrollment));
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
        eventPublisher.publishEvent(new EnrollmentRejectEvent(enrollment));
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.attended();
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.notAttended();
    }
}
