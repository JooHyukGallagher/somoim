package me.weekbelt.runningflex.modules.notification.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.notification.Notification;
import me.weekbelt.runningflex.modules.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(Notification::checked);
        notificationRepository.saveAll(notifications);
    }
}
