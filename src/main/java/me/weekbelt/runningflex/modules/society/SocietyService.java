package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.societyManager.SocietyManager;
import me.weekbelt.runningflex.modules.societyManager.SocietyManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SocietyService {

    private final SocietyRepository societyRepository;
    private final SocietyManagerRepository societyManagerRepository;

    public Society createNewSociety(Society society, Account account) {
        SocietyManager societyManager = SocietyManager.createSocietyManager(account);

//        Society newSociety = societyRepository.save(society);
//        SocietyManager societyManager = SocietyManager.createSocietyManager(account, newSociety);
//        societyManagerRepository.save(societyManager);
        return newSociety;
    }

    public Society findSocietyByPath(String path) {
        return societyRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException(path + "에 해당하는 동호회가 없습니다."));
    }
}
