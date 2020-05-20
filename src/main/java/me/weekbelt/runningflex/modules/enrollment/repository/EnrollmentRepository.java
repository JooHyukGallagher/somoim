package me.weekbelt.runningflex.modules.enrollment.repository;

import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.event.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>, EnrollmentRepositoryCustom {
    boolean existsByEventAndAccount(Event event, Account account);

    Optional<Enrollment> findByEventAndAccount(Event event, Account account);

    @EntityGraph("Enrollment.withEventAndSociety")
    List<Enrollment> findByAccountAndAcceptedOrderByEnrolledAtDesc(Account account, boolean accepted);
}
