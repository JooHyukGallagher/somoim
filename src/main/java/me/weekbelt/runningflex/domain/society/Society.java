package me.weekbelt.runningflex.domain.society;

import lombok.*;
import me.weekbelt.runningflex.domain.societyManager.SocietyManager;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Society {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "society")
    private List<SocietyManager> societyManagers = new ArrayList<>();

//    @OneToMany(mappedBy = "society")
//    private List<GroupMember> groupMembers = new ArrayList<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

//    @OneToMany(mappedBy = "society")
//    private List<GroupTag> groupTags = new ArrayList<>();
//
//    @OneToMany(mappedBy = "society")
//    private List<GroupZone> groupZones = new ArrayList<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;
}
