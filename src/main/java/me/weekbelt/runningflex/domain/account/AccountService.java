package me.weekbelt.runningflex.domain.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.web.dto.account.SignUpForm;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        // 검증된 입력값을 통해 엔티티 생성
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .emailVerified(false)
                .runningCreatedByWeb(true)
                .runningEnrollmentResultByEmail(true)
                .runningUpdatedByWeb(true)
                .build();

        // 생성한 엔티티를 DB에 저장
        return accountRepository.save(account);
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        // 메일 메시지 만들기
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("RunningFlex, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail());

        // 메일 메시지 보내기
        javaMailSender.send(mailMessage);
    }
}
