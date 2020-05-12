package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.form.SocietyDescriptionForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

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

    public Society getSocietyToUpdate(Account account, String path) throws AccessDeniedException {
        Society society = this.getSociety(path);
        if (!account.isManagerOf(society)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
        return society;
    }

    public void updateSocietyDescription(Society society, SocietyDescriptionForm societyDescriptionForm) {
        society.updateDescription(societyDescriptionForm);
    }

    public void updateSocietyImage(Society society, String image) {
        society.updateSocietyImage(image);
    }

    public void enableSocietyBanner(Society society) {
        society.enableSocietyBanner();
    }

    public void disableSocietyBanner(Society society) {
        society.disableSocietyBanner();
    }
}
