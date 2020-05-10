package me.weekbelt.runningflex.modules.society;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface SocietyRepository extends JpaRepository<Society, Long> {
    boolean existsByPath(String path);

    @EntityGraph(value = "Society.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Society> findByPath(String path);
}
