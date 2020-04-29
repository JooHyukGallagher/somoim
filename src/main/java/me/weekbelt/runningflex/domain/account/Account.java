package me.weekbelt.runningflex.domain.account;

import lombok.*;
import me.weekbelt.runningflex.web.dto.account.Profile;

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

    private LocalDateTime emailCheckTokenGeneratedAt;

    public Account() {
        this.runningCreatedByWeb = true;
        this.runningEnrollmentResultByWeb = true;
        this.runningUpdatedByWeb = true;
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
}
