package me.weekbelt.runningflex.modules.society.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class SocietyUpdateEvent {

    private final Society society;
    private final String message;

}
