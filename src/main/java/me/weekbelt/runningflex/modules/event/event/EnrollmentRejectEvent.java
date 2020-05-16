package me.weekbelt.runningflex.modules.event.event;

import me.weekbelt.runningflex.modules.enrollment.Enrollment;

public class EnrollmentRejectEvent extends EnrollmentEvent {

    public EnrollmentRejectEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청을 거절했습니다.");
    }
}
