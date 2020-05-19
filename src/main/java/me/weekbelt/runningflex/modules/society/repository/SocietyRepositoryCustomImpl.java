package me.weekbelt.runningflex.modules.society.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.QAccount;
import me.weekbelt.runningflex.modules.society.QSociety;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.QTag;
import me.weekbelt.runningflex.modules.zone.QZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SocietyRepositoryCustomImpl extends QuerydslRepositorySupport implements SocietyRepositoryCustom {

    public SocietyRepositoryCustomImpl() {
        super(Society.class);
    }

    @Override
    public Page<Society> findByKeyword(String keyword, Pageable pageable) {
        QSociety society = QSociety.society;
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
}
