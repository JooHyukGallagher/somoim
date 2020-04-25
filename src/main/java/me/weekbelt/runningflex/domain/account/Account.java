package me.weekbelt.runningflex.domain.account;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder @AllArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;
    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean runningCreatedByEmail;
    private boolean runningCreatedByWeb;

    private boolean runningEnrollmentResultByEmail;
    private boolean runningEnrollmentResultByWeb;

    private boolean runningUpdatedByEmail;
    private boolean runningUpdatedByWeb;

    public Account() {
        this.runningCreatedByWeb = true;
        this.runningEnrollmentResultByWeb = true;
        this.runningUpdatedByWeb = true;
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
