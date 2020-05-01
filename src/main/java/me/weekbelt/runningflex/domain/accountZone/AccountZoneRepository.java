package me.weekbelt.runningflex.domain.accountZone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountZoneRepository extends JpaRepository<AccountZone, Long> {

    @Query("select az from AccountZone az" +
            " join fetch  az.account a" +
            " join fetch az.zone z" +
            " where a.id = :accountId")
    List<AccountZone> findByAccountId(@Param("accountId") Long accountId);
}
