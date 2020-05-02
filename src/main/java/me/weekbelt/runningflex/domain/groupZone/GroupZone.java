package me.weekbelt.runningflex.domain.groupZone;

import lombok.*;
import me.weekbelt.runningflex.domain.group.Group;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.zone.Zone;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class GroupZone {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
