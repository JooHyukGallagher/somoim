package me.weekbelt.runningflex.modules.society.repository;

import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface SocietyRepositoryCustom {

    List<Society> findByKeyword(String keyword);
}
