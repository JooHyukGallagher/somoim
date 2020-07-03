package me.weekbelt.runningflex.modules.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.repository.AccountRepository;
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
                .password("12345678")
                .build();
        return accountRepository.save(account);
    }
}
