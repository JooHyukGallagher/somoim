package me.weekbelt.runningflex.modules.enrollment.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.runningflex.infra.config.AppProperties;
import me.weekbelt.runningflex.infra.mail.EmailMessage;
import me.weekbelt.runningflex.infra.mail.EmailService;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.event.Event;
import me.weekbelt.runningflex.modules.notification.Notification;
import me.weekbelt.runningflex.modules.notification.repository.NotificationRepository;
import me.weekbelt.runningflex.modules.notification.NotificationType;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Async
@Transactional
@Component
public class EnrollmentEventListener {

    private final NotificationRepository notificationRepository;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    @EventListener
    public void handleEnrollmentEvent(EnrollmentEvent enrollmentEvent) {
        Enrollment enrollment = enrollmentEvent.getEnrollment();
        Account account = enrollment.getAccount();
        Event event = enrollment.getEvent();
        Society society = event.getSociety();

        if (account.isSocietyEnrollmentResultByEmail()){
            sendEmail(enrollmentEvent, account, event, society);
        }

        if (account.isSocietyEnrollmentResultByWeb()) {
            createNotification(enrollmentEvent, account, event, society);
        }
    }


    private void sendEmail(EnrollmentEvent enrollmentEvent, Account account, Event event,
                           Society society) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/society/" + society.getEncodedPath() + "/events/" + event.getId());
        context.setVariable("linkName", society.getTitle());
        context.setVariable("message", enrollmentEvent.getMessage());
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("스터디올래, " + event.getTitle() + " 모임 참가 신청 결과입니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    private void createNotification(EnrollmentEvent enrollmentEvent, Account account, Event event,
                                    Society society) {
        Notification notification = Notification.builder()
                .title(society.getTitle() + " / " + event.getTitle())
                .link("/society/" + society.getEncodedPath() + "/events/" + event.getId())
                .checked(false)
                .createdDateTime(LocalDateTime.now())
                .message(enrollmentEvent.getMessage())
                .account(account)
                .notificationType(NotificationType.EVENT_ENROLLMENT)
                .build();
        notificationRepository.save(notification);
    }
}
