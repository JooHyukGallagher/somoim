package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SocietyFactory {

    @Autowired SocietyService societyService;
    @Autowired SocietyRepository societyRepository;

    public Society createSociety(String path, Account manager) {
        Society society = Society.builder()
                .path(path)
                .build();
        societyService.createNewSociety(society, manager);
        return society;
    }
}
