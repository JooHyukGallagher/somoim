package me.weekbelt.runningflex.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.runningflex.domain.accountTag.AccountTag;
import me.weekbelt.runningflex.domain.accountTag.AccountTagRepository;
import me.weekbelt.runningflex.domain.accountZone.AccountZone;
import me.weekbelt.runningflex.domain.accountZone.AccountZoneRepository;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.tag.TagRepository;
import me.weekbelt.runningflex.domain.zone.Zone;
import me.weekbelt.runningflex.mail.EmailMessage;
import me.weekbelt.runningflex.mail.EmailService;
import me.weekbelt.runningflex.web.dto.account.Notifications;
import me.weekbelt.runningflex.web.dto.account.Profile;
import me.weekbelt.runningflex.web.dto.account.SignUpForm;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTagRepository accountTagRepository;
    private final AccountZoneRepository accountZoneRepository;
    private final EmailService emailService;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
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
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("RunningFlex, 회원 가입 인증")
                .message("/check-email-token?token=" + newAccount.getEmailCheckToken() +  // message는 나중에 HTML로 수정
                        "&email=" + newAccount.getEmail())
                .build();

        emailService.sendEmail(emailMessage);
    }

    public void sendLoginLink(Account account) {
        account.generateEmailCheckToken();;
        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("RunningFlex, 로그인 링크")
                .message("/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail())
                .build();

        emailService.sendEmail(emailMessage);
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

    public List<Zone> getZones(Account account) {
        List<AccountZone> accountZones = accountZoneRepository.findByAccountId(account.getId());
        return accountZones.stream().map(AccountZone::getZone).collect(Collectors.toList());
    }

    public void addZone(Account account, Zone zone) {
        accountZoneRepository.save(AccountZone.builder().account(account).zone(zone).build());
    }

    public void removeZone(Account account, Zone zone) {
        List<AccountZone> accountZones = accountZoneRepository.findByAccountId(account.getId());
        for (AccountZone accountZone : accountZones) {
            if (accountZone.getZone().getCity().equals(zone.getCity()) &&
                    accountZone.getZone().getLocalNameOfCity().equals(zone.getLocalNameOfCity()) &&
                    accountZone.getZone().getProvince().equals(zone.getProvince())) {
                accountZoneRepository.delete(accountZone);
                break;
            }
        }
    }
}
