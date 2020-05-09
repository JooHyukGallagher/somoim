package me.weekbelt.runningflex.modules.tag;

import lombok.*;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

//    @OneToMany(mappedBy = "tag")
//    private List<AccountTag> accountTags = new ArrayList<>();
//
//    @OneToMany(mappedBy = "tag")
//    private List<SocietyTag> societyTags = new ArrayList<>();
}
