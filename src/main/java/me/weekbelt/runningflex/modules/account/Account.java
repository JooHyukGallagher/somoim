package me.weekbelt.runningflex.modules.account;

import lombok.*;
import me.weekbelt.runningflex.modules.account.form.Notifications;
import me.weekbelt.runningflex.modules.account.form.Profile;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean groupCreatedByEmail;
    private boolean groupCreatedByWeb = true;

    private boolean groupEnrollmentResultByEmail;
    private boolean groupEnrollmentResultByWeb = true;

    private boolean groupUpdatedByEmail;
    private boolean groupUpdatedByWeb = true;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    @Builder
    public Account(String email, String nickname, String password, boolean emailVerified,
                   String emailCheckToken, LocalDateTime joinedAt,
                   boolean groupCreatedByWeb, boolean groupCreatedByEmail,
                   boolean groupEnrollmentResultByWeb, boolean groupEnrollmentResultByEmail,
                   boolean groupUpdatedByWeb, boolean groupUpdatedByEmail) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.emailVerified = emailVerified;
        this.emailCheckToken = emailCheckToken;
        this.joinedAt = joinedAt;
        this.groupCreatedByWeb = groupCreatedByWeb;
        this.groupCreatedByEmail = groupCreatedByEmail;
        this.groupEnrollmentResultByWeb = groupEnrollmentResultByWeb;
        this.groupEnrollmentResultByEmail = groupEnrollmentResultByEmail;
        this.groupUpdatedByWeb = groupUpdatedByWeb;
        this.groupUpdatedByEmail = groupUpdatedByEmail;
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(10));
    }

    public void updateProfile(Profile profile){
        this.url = profile.getUrl();
        this.occupation = profile.getOccupation();
        this.location = profile.getLocation();
        this.bio = profile.getBio();
        this.profileImage = profile.getProfileImage();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNotifications(Notifications notifications) {
        this.groupCreatedByEmail = notifications.isGroupCreatedByEmail();
        this.groupCreatedByWeb = notifications.isGroupCreatedByWeb();
        this.groupUpdatedByEmail = notifications.isGroupUpdatedByEmail();
        this.groupUpdatedByWeb = notifications.isGroupUpdatedByWeb();
        this.groupEnrollmentResultByEmail = notifications.isGroupEnrollmentResultByEmail();
        this.groupEnrollmentResultByWeb = notifications.isGroupEnrollmentResultByWeb();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isManagerOf(Society society) {
        return society.getManagers().contains(this);
    }
}
