package me.weekbelt.runningflex.modules.society.repository;

import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface SocietyRepository extends JpaRepository<Society, Long>, SocietyRepositoryCustom {
    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"tags", "zones", "managers", "members"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Society> findByPath(String path);

    @EntityGraph(attributePaths = {"tags", "managers"})
    Optional<Society> findAccountWithTagsByPath(String path);

    @EntityGraph(attributePaths = {"zones", "managers"})
    Optional<Society> findSocietyWithZonesByPath(String path);

    @EntityGraph(attributePaths = "managers")
    Optional<Society> findSocietyWithManagersByPath(String path);

    @EntityGraph(attributePaths = "members")
    Optional<Society> findSocietyWithMembersByPath(String path);

    Optional<Society> findSocietyOnlyByPath(String path);

    @EntityGraph(attributePaths = {"tags", "zones"})
    Society findSocietyWithTagsAndZonesById(Long id);

    @EntityGraph(attributePaths = {"members", "managers"})
    Society findSocietyWithManagersAndMembersById(Long id);

    @EntityGraph(attributePaths = {"zones", "tags"})
    List<Society> findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean published, boolean closed);

    List<Society> findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean checked);

    List<Society> findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean checked);
}


