package me.weekbelt.runningflex.domain.group;

import lombok.*;
import me.weekbelt.runningflex.domain.groupManager.GroupManager;
import me.weekbelt.runningflex.domain.groupMember.GroupMember;
import me.weekbelt.runningflex.domain.groupTag.GroupTag;
import me.weekbelt.runningflex.domain.groupZone.GroupZone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "manager")
    private List<GroupManager> groupManagers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @OneToMany(mappedBy = "group")
    private List<GroupTag> groupTags = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<GroupZone> groupZones = new ArrayList<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recrutingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;
}
