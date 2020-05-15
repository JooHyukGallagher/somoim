package me.weekbelt.runningflex.modules.notification;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.weekbelt.runningflex.modules.account.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
