package me.weekbelt.runningflex.domain.accountTag;

import lombok.*;
import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.tag.Tag;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class AccountTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
