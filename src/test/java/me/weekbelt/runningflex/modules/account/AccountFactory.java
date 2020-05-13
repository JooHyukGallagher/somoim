package me.weekbelt.runningflex.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountFactory {
    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account account = Account.builder()
                .nickname(nickname)
                .email(nickname + "@email.com")
                .build();
        return accountRepository.save(account);
    }
}
