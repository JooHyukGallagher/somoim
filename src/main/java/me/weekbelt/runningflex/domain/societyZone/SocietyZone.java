package me.weekbelt.runningflex.domain.societyZone;

import lombok.*;
import me.weekbelt.runningflex.domain.society.Society;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.zone.Zone;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class SocietyZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
