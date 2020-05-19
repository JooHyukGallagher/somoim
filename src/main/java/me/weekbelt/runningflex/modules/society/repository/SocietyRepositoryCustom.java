package me.weekbelt.runningflex.modules.society.repository;

import me.weekbelt.runningflex.modules.society.Society;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SocietyRepositoryCustom {

    Page<Society> findByKeyword(String keyword, Pageable pageable);
}
