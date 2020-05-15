package me.weekbelt.runningflex.modules.event;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EventFactory {
    @Autowired
    EventRepository eventRepository;

    public Event createEvent(String title, EventType eventType, Integer limitOfEnrollments,
                             Society society, Account createdBy) {

        LocalDateTime now = LocalDateTime.now();
        Event event = Event.builder()
                .title(title)
                .eventType(eventType)
                .limitOfEnrollments(limitOfEnrollments)
                .createdBy(createdBy)
                .society(society)
                .createdDateTime(now)
                .endEnrollmentDateTime(now.plusDays(1))
                .startDateTime(now.plusDays(1).plusHours(5))
                .endDateTime(now.plusDays(1).plusHours(7))
                .build();
        return eventRepository.save(event);
    }
}
