package me.weekbelt.runningflex.modules.account.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean societyCreatedByEmail;
    private boolean societyCreatedByWeb;

    private boolean societyEnrollmentResultByEmail;
    private boolean societyEnrollmentResultByWeb;

    private boolean societyUpdatedByEmail;
    private boolean societyUpdatedByWeb;

}
