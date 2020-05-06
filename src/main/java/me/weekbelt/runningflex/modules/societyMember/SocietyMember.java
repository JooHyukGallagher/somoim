package me.weekbelt.runningflex.modules.societyMember;

import lombok.*;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class SocietyMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;
}
