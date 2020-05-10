package me.weekbelt.runningflex.modules.society;

import lombok.*;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.UserAccount;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "Society.withAll", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Account> managers = new ArrayList<>();

    @ManyToMany
    private List<Account> members = new ArrayList<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    private List<Zone> zones = new ArrayList<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

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
}
