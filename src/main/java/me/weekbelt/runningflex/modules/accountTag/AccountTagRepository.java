package me.weekbelt.runningflex.modules.accountTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountTagRepository extends JpaRepository<AccountTag, Long> {

    @Query("select at from AccountTag at " +
            "join fetch at.account a " +
            "join fetch at.tag t where a.id = :accountId")
    List<AccountTag> findByAccountId(@Param("accountId") Long accountId);



}
