package me.weekbelt.runningflex.modules.societyManager;

import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SocietyManagerRepository extends JpaRepository<SocietyManager, Long> {
    List<SocietyManager> findBySociety(Society society);
}
