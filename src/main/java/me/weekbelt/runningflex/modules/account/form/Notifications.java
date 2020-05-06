package me.weekbelt.runningflex.modules.account.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean groupCreatedByEmail;
    private boolean groupCreatedByWeb;

    private boolean groupEnrollmentResultByEmail;
    private boolean groupEnrollmentResultByWeb;

    private boolean groupUpdatedByEmail;
    private boolean groupUpdatedByWeb;

}
