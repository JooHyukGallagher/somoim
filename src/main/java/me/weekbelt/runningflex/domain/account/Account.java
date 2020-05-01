package me.weekbelt.runningflex.domain.account;

import lombok.*;
import me.weekbelt.runningflex.domain.accountTag.AccountTag;
import me.weekbelt.runningflex.domain.accountZone.AccountZone;
import me.weekbelt.runningflex.web.dto.account.Notifications;
import me.weekbelt.runningflex.web.dto.account.Profile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue
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

    @OneToMany(mappedBy = "account")
    private List<AccountTag> accountTags = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<AccountZone> accountZones = new ArrayList<>();

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
}
