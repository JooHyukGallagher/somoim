package me.weekbelt.runningflex.modules.society.event;

import lombok.Getter;
import me.weekbelt.runningflex.modules.society.Society;

@Getter
public class SocietyCreatedEvent {

    private Society society;

    public SocietyCreatedEvent(Society society) {
        this.society = society;
    }
}
