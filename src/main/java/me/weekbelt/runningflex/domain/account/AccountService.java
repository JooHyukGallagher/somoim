package me.weekbelt.runningflex.domain.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.domain.accountTag.AccountTag;
import me.weekbelt.runningflex.domain.accountTag.AccountTagRepository;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.tag.TagRepository;
import me.weekbelt.runningflex.web.dto.account.Notifications;
import me.weekbelt.runningflex.web.dto.account.Profile;
import me.weekbelt.runningflex.web.dto.account.SignUpForm;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final AccountTagRepository accountTagRepository;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
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
                .groupCreatedByWeb(true)
                .groupEnrollmentResultByWeb(true)
                .groupUpdatedByWeb(true)
                .build();
        account.generateEmailCheckToken();
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

    @Transactional(readOnly = true)
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

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        account.updateProfile(profile);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.updatePassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        account.updateNotifications(notifications);
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname) {
        account.updateNickname(nickname);
        accountRepository.save(account);

        login(account); // dropdown의 닉네임이 바꿔지도록 하기 위해
    }

    public void sendLoginLink(Account account) {
        account.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("소모임(가제), 로그인 링크");
        mailMessage.setText("/login-by-email?token=" + account.getEmailCheckToken() +
                "&email=" + account.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void addTag(Account account, Tag tag) {
        accountTagRepository.save(AccountTag.builder().account(account).tag(tag).build());
    }

    public List<Tag> getTags(Account account) {
        List<AccountTag> accountTags = accountTagRepository.findByAccountId(account.getId());
        return accountTags.stream().map(AccountTag::getTag).collect(Collectors.toList());
    }

    public void removeTag(Account account, Tag tag) {
        List<AccountTag> accountTags = accountTagRepository.findByAccountId(account.getId());
        for (AccountTag accountTag : accountTags) {
            if (accountTag.getTag().getTitle().equals(tag.getTitle())) {
                accountTagRepository.delete(accountTag);
                break;
            }
        }
    }
}
