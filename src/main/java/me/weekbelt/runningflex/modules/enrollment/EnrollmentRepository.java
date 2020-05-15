package me.weekbelt.runningflex.modules.enrollment;

import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByEventAndAccount(Event event, Account account);

    Optional<Enrollment> findByEventAndAccount(Event event, Account account);
}
