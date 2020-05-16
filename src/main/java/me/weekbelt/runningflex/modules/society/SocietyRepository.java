package me.weekbelt.runningflex.modules.society;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface SocietyRepository extends JpaRepository<Society, Long> {
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
}
