package me.weekbelt.runningflex.modules.account;

import com.querydsl.core.types.Predicate;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;

import java.util.Set;

import static me.weekbelt.runningflex.modules.account.QAccount.*;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        return account.zones.any().in(zones)
                .and(account.tags.any().in(tags));
    }
}
