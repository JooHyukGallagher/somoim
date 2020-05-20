package me.weekbelt.runningflex.modules.society.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.QTag;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.QZone;
import me.weekbelt.runningflex.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

import static me.weekbelt.runningflex.modules.society.QSociety.society;

public class SocietyRepositoryCustomImpl extends QuerydslRepositorySupport implements SocietyRepositoryCustom {

    public SocietyRepositoryCustomImpl() {
        super(Society.class);
    }

    @Override
    public Page<Society> findByKeyword(String keyword, Pageable pageable) {
        JPQLQuery<Society> query = from(society)
                .where(society.published.isTrue()
                        .and(society.title.containsIgnoreCase(keyword))
                        .or(society.tags.any().title.containsIgnoreCase(keyword))
                        .or(society.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(society.tags, QTag.tag).fetchJoin()
                .leftJoin(society.zones, QZone.zone).fetchJoin()
                .distinct();

        JPQLQuery<Society> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Society> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Society> findByAccount(Set<Tag> tags, Set<Zone> zones) {
        JPQLQuery<Society> query = from(society)
                .where(society.published.isTrue()
                        .and(society.closed.isFalse())
                        .and(society.tags.any().in(tags))
                        .and(society.zones.any().in(zones)))
                .leftJoin(society.tags, QTag.tag).fetchJoin()
                .leftJoin(society.zones, QZone.zone).fetchJoin()
                .orderBy(society.publishedDateTime.desc())
                .distinct()
                .limit(9);
        return query.fetch();
    }
}
