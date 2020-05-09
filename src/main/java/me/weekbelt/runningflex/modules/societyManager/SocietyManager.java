package me.weekbelt.runningflex.modules.societyManager;

import lombok.*;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class SocietyManager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;

    public static SocietyManager createSocietyManager(Account manager) {
        return SocietyManager.builder()
                .manager(manager)
                .build();
    }

//    public static SocietyManager createSocietyManager(Account manager, Society society) {
//        SocietyManager societyManager = SocietyManager.builder()
//                .manager(manager)
//                .society(society)
//                .build();
//        return societyManager;
//    }

}
