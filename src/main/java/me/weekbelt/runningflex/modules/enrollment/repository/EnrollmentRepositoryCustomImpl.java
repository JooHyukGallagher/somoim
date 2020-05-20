package me.weekbelt.runningflex.modules.enrollment.repository;

import com.querydsl.jpa.JPQLQuery;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.enrollment.QEnrollment;
import me.weekbelt.runningflex.modules.event.QEvent;
import me.weekbelt.runningflex.modules.society.QSociety;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class EnrollmentRepositoryCustomImpl extends QuerydslRepositorySupport implements EnrollmentRepositoryCustom {

    public EnrollmentRepositoryCustomImpl() {
        super(Enrollment.class);
    }

//    @Override
//    public List<Enrollment> findByAccountAndAcceptedOrderByEnrollmentAtDesc(Account account, boolean accepted) {
//        QEnrollment enrollment = QEnrollment.enrollment;
//        JPQLQuery<Enrollment> query = from(enrollment)
//                .where(enrollment.account.eq(account).and(enrollment.accepted.isTrue()))
//                .leftJoin(enrollment.event, QEvent.event)
//                .on(enrollment.event.id.eq(QEvent.event.id)).fetchJoin()
//                .leftJoin(enrollment.event.society, QSociety.society)
//                .on(enrollment.event.society.id.eq(QSociety.society.id)).fetchJoin()
//                .orderBy(enrollment.enrolledAt.desc());
//
//        return query.fetch();
//    }
}
