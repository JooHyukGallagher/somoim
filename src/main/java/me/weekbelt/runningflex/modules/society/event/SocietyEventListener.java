package me.weekbelt.runningflex.modules.society.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.runningflex.infra.config.AppProperties;
import me.weekbelt.runningflex.infra.mail.EmailMessage;
import me.weekbelt.runningflex.infra.mail.EmailService;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountPredicates;
import me.weekbelt.runningflex.modules.account.AccountRepository;
import me.weekbelt.runningflex.modules.notification.Notification;
import me.weekbelt.runningflex.modules.notification.NotificationRepository;
import me.weekbelt.runningflex.modules.notification.NotificationType;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyRepository;
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
public class SocietyEventListener {

    private final SocietyRepository societyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleSocietyCreatedEvent(SocietyCreatedEvent societyCreatedEvent) {
        Society society = societyRepository
                .findSocietyWithTagsAndZonesById(societyCreatedEvent.getSociety().getId());
        Iterable<Account> accounts = accountRepository
                .findAll(AccountPredicates.findByTagsAndZones(society.getTags(), society.getZones()));
        accounts.forEach(account -> {
            if (account.isSocietyCreatedByEmail()) {
                sendCreatedEmail(society, account);
            }
            if (account.isSocietyCreatedByWeb()) {
                saveSocietyNotifications(society, account);
            }
        });
    }

    private void sendCreatedEmail(Society society, Account account) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/society/" + society.getEncodedPath());
        context.setVariable("linkname", society.getTitle());
        context.setVariable("message", "새로운 소모임이 생겼습니다.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("somoim, '" + society.getTitle() + "' 소모임이 생겼습니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    private void saveSocietyNotifications(Society society, Account account) {
        Notification notification = Notification.builder()
                .title(society.getTitle())
                .link("/society/" + society.getEncodedPath())
                .checked(false)
                .createdDateTime(LocalDateTime.now())
                .message(society.getShortDescription())
                .account(account)
                .notificationType(NotificationType.SOCIETY_CREATED)
                .build();
        notificationRepository.save(notification);
    }
}
