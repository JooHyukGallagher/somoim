package me.weekbelt.runningflex.web.dto.account;

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
