package me.weekbelt.runningflex.modules.event;

import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = "enrollments")
    List<Event> findBySocietyOrderByStartDateTime(Society society);
}
