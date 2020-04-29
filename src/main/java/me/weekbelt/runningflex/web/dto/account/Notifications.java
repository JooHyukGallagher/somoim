package me.weekbelt.runningflex.web.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.weekbelt.runningflex.domain.account.Account;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean groupCreatedByEmail;
    private boolean groupCreatedByWeb;

    private boolean groupEnrollmentResultByEmail;
    private boolean groupEnrollmentResultByWeb;

    private boolean groupUpdatedByEmail;
    private boolean groupUpdatedByWeb;

    public Notifications(Account account) {
        this.groupCreatedByEmail = account.isGroupCreatedByEmail();
        this.groupCreatedByWeb = account.isGroupCreatedByWeb();
        this.groupEnrollmentResultByEmail = account.isGroupEnrollmentResultByEmail();
        this.groupEnrollmentResultByWeb = account.isGroupEnrollmentResultByWeb();
        this.groupUpdatedByEmail = account.isGroupUpdatedByEmail();
        this.groupUpdatedByWeb = account.isGroupUpdatedByWeb();
    }
}
