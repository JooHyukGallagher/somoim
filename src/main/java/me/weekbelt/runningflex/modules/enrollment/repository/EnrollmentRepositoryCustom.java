package me.weekbelt.runningflex.modules.enrollment.repository;

import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EnrollmentRepositoryCustom {

//    List<Enrollment> findByAccountAndAcceptedOrderByEnrollmentAtDesc(Account account, boolean accepted);

}
