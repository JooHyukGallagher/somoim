package me.weekbelt.runningflex.modules.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.UserAccount;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.event.form.EventForm;
import me.weekbelt.runningflex.modules.society.Society;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Society society;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column
    private Integer limitOfEnrollments;

    @OneToMany(mappedBy = "event")
    @OrderBy("enrolledAt")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder
    public Event(Society society, Account createdBy, String title, String description,
                 LocalDateTime createdDateTime, LocalDateTime endEnrollmentDateTime,
                 LocalDateTime startDateTime, LocalDateTime endDateTime,
                 Integer limitOfEnrollments, EventType eventType) {
        this.society = society;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.endEnrollmentDateTime = endEnrollmentDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.limitOfEnrollments = limitOfEnrollments;
        this.eventType = eventType;
    }

    public static Event createEventFromEventForm(EventForm eventForm){
        return Event.builder()
                .title(eventForm.getTitle())
                .description(eventForm.getDescription())
                .eventType(eventForm.getEventType())
                .endEnrollmentDateTime(eventForm.getEndEnrollmentDateTime())
                .startDateTime(eventForm.getStartDateTime())
                .endDateTime(eventForm.getEndDateTime())
                .limitOfEnrollments(eventForm.getLimitOfEnrollments())
                .build();
    }

    public void createEvent(Society society, Account createdBy) {
        this.createdBy = createdBy;
        this.createdDateTime = LocalDateTime.now();
        this.society = society;
    }

    public boolean isEnrollableFor(UserAccount userAccount) {
        return isNotClosed() && !this.isAttended(userAccount) && !isAlreadyEnrolled(userAccount);
    }

    public boolean isDisenrollableFor(UserAccount userAccount) {
        return isNotClosed() && !this.isAttended(userAccount) && isAlreadyEnrolled(userAccount);
    }

    private boolean isNotClosed() {
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    private boolean isAlreadyEnrolled(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        for (Enrollment enrollment : this.enrollments) {
            if (enrollment.getAccount().equals(account)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfRemainSpots() {
        return this.limitOfEnrollments - (int) this.enrollments.stream()
                .filter(Enrollment::isAccepted).count();
    }


    public void updateEvent(EventForm eventForm) {
        this.title = eventForm.getTitle();
        this.description = eventForm.getDescription();
        this.eventType = eventForm.getEventType();
        this.endEnrollmentDateTime = eventForm.getEndEnrollmentDateTime();
        this.startDateTime = eventForm.getStartDateTime();
        this.endDateTime = eventForm.getEndDateTime();
        this.limitOfEnrollments = eventForm.getLimitOfEnrollments();
    }

    public boolean canAccept(Enrollment enrollment) {
        return this.eventType == EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && !enrollment.isAccepted();
    }

    public boolean canReject(Enrollment enrollment) {
        return this.eventType == EventType.CONFIRMATIVE
                && !enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && enrollment.isAccepted();
    }


    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.addEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.addEvent(null);
    }

    public void acceptNextWaitingEnrollment() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            Enrollment enrollmentToAccept = this.getTheFirstWaitingEnrollment();
            if(enrollmentToAccept != null) {
                enrollmentToAccept.accepted();
            }
        }
    }

    private Enrollment getTheFirstWaitingEnrollment() {
        for (Enrollment enrollment : enrollments) {
            if (!enrollment.isAccepted()) {
                return enrollment;
            }
        }
        return null;
    }

    public void acceptWaitingList() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            var waitingList = getWaitingList();
            int numberToAccept = (int) Math.min(this.limitOfEnrollments -
                    this.getNumberOfAcceptedEnrollments(), waitingList.size());
            waitingList.subList(0, numberToAccept).forEach(Enrollment::accepted);
        }
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        return this.eventType == EventType.FCFS
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments();
    }

    private List<Enrollment> getWaitingList() {
        return this.enrollments.stream()
                .filter(enrollment -> !enrollment.isAccepted())
                .collect(Collectors.toList());
    }

    public boolean isAttended(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getAccount().equals(account) && enrollment.isAttended()) {
                return true;
            }
        }

        return false;
    }

    public void accept(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()){
            enrollment.accepted();
        }
    }

    public long getNumberOfAcceptedEnrollments() {
        return this.enrollments.stream().filter(Enrollment::isAccepted).count();
    }

    public void reject(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE) {
            enrollment.notAccepted();
        }
    }
}
