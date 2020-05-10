package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SocietyService {

    private final SocietyRepository societyRepository;

    public Society createNewSociety(Society society, Account account) {
        Society newSociety = societyRepository.save(society);
        newSociety.addManager(account);
        return newSociety;
    }

    public Society getSociety(String path) {
        return societyRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException(path + "에 해당하는 동호회가 없습니다."));
    }
}
