package me.weekbelt.runningflex.modules.notification;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link;

    private String message;

    private boolean checked;

    @ManyToOne
    private Account account;

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Builder
    public Notification(String title, String link, String message, boolean checked,
                        Account account, LocalDateTime createdDateTime, NotificationType notificationType) {
        this.title = title;
        this.link = link;
        this.message = message;
        this.checked = checked;
        this.account = account;
        this.createdDateTime = createdDateTime;
        this.notificationType = notificationType;
    }
}
