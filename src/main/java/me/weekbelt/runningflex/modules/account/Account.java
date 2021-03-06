package me.weekbelt.runningflex.modules.account;

import lombok.*;
import me.weekbelt.runningflex.modules.account.form.Notifications;
import me.weekbelt.runningflex.modules.account.form.Profile;
import me.weekbelt.runningflex.modules.account.form.SignUpForm;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
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

    private boolean societyCreatedByEmail;
    private boolean societyCreatedByWeb = true;

    private boolean societyEnrollmentResultByEmail;
    private boolean societyEnrollmentResultByWeb = true;

    private boolean societyUpdatedByEmail;
    private boolean societyUpdatedByWeb = true;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    @Builder
    public Account(String email, String nickname, String password, boolean emailVerified,
                   String emailCheckToken, LocalDateTime joinedAt,
                   boolean societyCreatedByWeb, boolean societyCreatedByEmail,
                   boolean societyEnrollmentResultByWeb, boolean societyEnrollmentResultByEmail,
                   boolean societyUpdatedByWeb, boolean societyUpdatedByEmail) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.emailVerified = emailVerified;
        this.emailCheckToken = emailCheckToken;
        this.joinedAt = joinedAt;
        this.societyCreatedByWeb = societyCreatedByWeb;
        this.societyCreatedByEmail = societyCreatedByEmail;
        this.societyEnrollmentResultByWeb = societyEnrollmentResultByWeb;
        this.societyEnrollmentResultByEmail = societyEnrollmentResultByEmail;
        this.societyUpdatedByWeb = societyUpdatedByWeb;
        this.societyUpdatedByEmail = societyUpdatedByEmail;
    }

    public static Account createAccountFromSignUpForm(SignUpForm signUpForm, PasswordEncoder passwordEncoder) {
        return Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .emailVerified(false)
                .societyCreatedByWeb(true)
                .societyEnrollmentResultByWeb(true)
                .societyUpdatedByWeb(true)
                .build();
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

    public void updateProfile(Profile profile) {
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
        this.societyCreatedByEmail = notifications.isSocietyCreatedByEmail();
        this.societyCreatedByWeb = notifications.isSocietyCreatedByWeb();
        this.societyUpdatedByEmail = notifications.isSocietyUpdatedByEmail();
        this.societyUpdatedByWeb = notifications.isSocietyUpdatedByWeb();
        this.societyEnrollmentResultByEmail = notifications.isSocietyEnrollmentResultByEmail();
        this.societyEnrollmentResultByWeb = notifications.isSocietyEnrollmentResultByWeb();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isManagerOf(Society society) {
        return society.getManagers().contains(this);
    }
}
