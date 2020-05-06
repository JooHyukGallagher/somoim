package me.weekbelt.runningflex.modules.societyTag;

import lombok.*;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.Tag;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class SocietyTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
