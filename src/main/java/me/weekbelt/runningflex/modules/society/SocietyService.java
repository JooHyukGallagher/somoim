package me.weekbelt.runningflex.modules.society;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.society.form.SocietyDescriptionForm;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.zone.Zone;
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

    public Society getSocietyToUpdateTag(Account account, String path) throws AccessDeniedException {
        Society society = societyRepository.findAccountWithTagsByPath(path)
                .orElse(null);
        checkIfExistingSociety(path, society);
        checkIfManager(account, society);
        return society;
    }

    public void addTag(Society society, Tag tag) {
        society.getTags().add(tag);
    }

    public void removeTag(Society society, Tag tag) {
        society.getTags().remove(tag);
    }

    public Society getSocietyToUpdateZone(Account account, String path) throws AccessDeniedException {
        Society society = societyRepository.findSocietyWithZonesByPath(path)
                .orElse(null);
        checkIfExistingSociety(path, society);
        checkIfManager(account, society);
        return society;
    }


    public void addZone(Society society, Zone zone) {
        society.getZones().add(zone);
    }

    public void removeZone(Society society, Zone zone) {
        society.getZones().remove(zone);
    }

    public Society getSocietyToUpdateStatus(Account account, String path) throws AccessDeniedException {
        Society society = societyRepository.findSocietyWithManagersByPath(path)
                .orElse(null);
        checkIfExistingSociety(path, society);
        checkIfManager(account, society);
        return society;
    }

    private void checkIfExistingSociety(String path, Society society) {
        if (society == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
    }

    private void checkIfManager(Account account, Society society) throws AccessDeniedException {
        if (!society.isManagedBy(account)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    public void publish(Society society) {
        society.publish();
    }

    public void close(Society society) {
        society.close();
    }

    public void startRecruit(Society society) {
        society.startRecruit();
    }

    public void stopRecruit(Society society) {
        society.stopRecruit();
    }
}
