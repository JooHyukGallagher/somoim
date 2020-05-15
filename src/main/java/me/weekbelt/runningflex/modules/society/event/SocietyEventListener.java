package me.weekbelt.runningflex.modules.society.event;

import lombok.extern.slf4j.Slf4j;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional
@Component
public class SocietyEventListener {

    @EventListener
    public void handleSocietyCreatedEvent(SocietyCreatedEvent societyCreatedEvent) {
        Society society = societyCreatedEvent.getSociety();
        log.info(society.getTitle() + "is created");
        // TODO 이메일을 보내거나. DB에 Notification 정보를 저장하면 됩니다.
    }
}
