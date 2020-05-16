package me.weekbelt.runningflex.modules.event.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    protected final Enrollment enrollment;

    protected final String message;
}
