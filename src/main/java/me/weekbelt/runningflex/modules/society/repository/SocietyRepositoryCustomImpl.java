package me.weekbelt.runningflex.modules.society.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.society.QSociety;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.tag.QTag;
import me.weekbelt.runningflex.modules.zone.QZone;

import java.util.List;

@RequiredArgsConstructor
public class SocietyRepositoryCustomImpl implements SocietyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Society> findByKeyword(String keyword) {
        QSociety society = QSociety.society;
        JPAQuery<Society> query = queryFactory
                .select(society).from(society)
                .where(society.published.isTrue()
                        .and(society.title.containsIgnoreCase(keyword))
                        .and(society.tags.any().title.containsIgnoreCase(keyword))
                        .and(society.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(society.tags, QTag.tag).fetchJoin()
                .leftJoin(society.zones, QZone.zone).fetchJoin()
                .distinct();
        return query.fetch();
    }
}
