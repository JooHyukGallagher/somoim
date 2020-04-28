package me.weekbelt.runningflex.domain.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.web.dto.account.SignUpForm;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
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

    public void sendSignUpConfirmEmail(Account newAccount) {
        // 메일 메시지 만들기
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("RunningFlex, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail());

        // 메일 메시지 보내기
        javaMailSender.send(mailMessage);
    }

    public void login(Account account) {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))); // ROLE_USER의 권한을 부여함
        context.setAuthentication(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname).orElse(null);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname)
                    .orElseThrow(() -> new UsernameNotFoundException("찾는 아이디나 이메일이 없습니다."));
        }

        return new UserAccount(account);
    }

    public Account findByNickname(String nickname) {
        return accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("찾는" + nickname + "이 없습니다."));
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
}
