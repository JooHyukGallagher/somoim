package me.weekbelt.runningflex.modules.event;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event, Society society, Account account) {
        event.createEvent(society, account);
        return eventRepository.save(event);
    }
}
