package me.weekbelt.runningflex.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNew(String title) {
        // 만약 태그가 DB에 없다면 저장하고 반환
        return tagRepository.findByTitle(title).orElseGet(
                () -> tagRepository.save(Tag.builder().title(title).build()));
    }

    public Tag findTagByTitle(String title) {
        return tagRepository.findByTitle(title).orElse(null);
    }
}
