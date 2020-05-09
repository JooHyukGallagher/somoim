package me.weekbelt.runningflex.modules.society;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface SocietyRepository extends JpaRepository<Society, Long> {
    boolean existsByPath(String path);

    Optional<Society> findByPath(String path);
}
