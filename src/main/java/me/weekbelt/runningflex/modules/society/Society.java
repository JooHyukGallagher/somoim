package me.weekbelt.runningflex.modules.society;

import lombok.*;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.UserAccount;
import me.weekbelt.runningflex.modules.society.form.SocietyDescriptionForm;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Society {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    @Builder
    public Society(String path, String title, String shortDescription, String fullDescription) {
        this.path = path;
        this.title = title;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
    }

    public String getEncodedPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account)
                && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public void updateDescription(SocietyDescriptionForm societyDescriptionForm) {
        this.shortDescription = societyDescriptionForm.getShortDescription();
        this.fullDescription = societyDescriptionForm.getFullDescription();
    }

    public void updateSocietyImage(String image) {
        this.image = image;
    }

    public void enableSocietyBanner() {
        this.useBanner = true;
    }

    public void disableSocietyBanner() {
        this.useBanner = false;
    }

    public boolean isManagedBy(Account account) {
        return this.managers.contains(account);
    }
}
