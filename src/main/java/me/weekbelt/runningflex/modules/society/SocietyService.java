package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.form.SocietyForm;
import me.weekbelt.runningflex.modules.societyManager.SocietyManager;
import me.weekbelt.runningflex.modules.societyManager.SocietyManagerRepository;
import me.weekbelt.runningflex.modules.societyMember.SocietyMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SocietyService {

    private final SocietyRepository societyRepository;
    private final SocietyManagerRepository societyManagerRepository;

    public Society createNewSociety(Society society, Account account) {
        Society newSociety = societyRepository.save(society);
        SocietyManager societyManager = SocietyManager.builder()
                .manager(account).society(newSociety)
                .build();
        societyManagerRepository.save(societyManager);
        return newSociety;
    }
}
