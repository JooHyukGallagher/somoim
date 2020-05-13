package me.weekbelt.runningflex.modules.event;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventFactory {
    @Autowired
    EventRepository eventRepository;

    public Event createEvent(Account createdBy, Society society) {
        Event event = Event.builder()
                .createdBy(createdBy)
                .society(society)
                .build();
        return eventRepository.save(event);
    }
}
