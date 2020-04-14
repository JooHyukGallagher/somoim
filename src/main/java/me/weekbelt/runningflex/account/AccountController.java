package me.weekbelt.runningflex.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.domain.account.Account;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        // 입력값 검증
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        // 검증된 입력값을 통해 엔티티 생성
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword())  // TODO: 나중에 패스워드 인코딩 필요
                .emailVerified(false)
                .runningEnrollmentResultByEmail(true)
                .runningUpdatedByWeb(true)
                .build();

        // 생성한 엔티티를 DB에 저장
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();   // 이메일체크 토큰 생성

        // 메일 메시지 만들기
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("RunningFlex, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail());

        // 메일 메시지 보내기
        javaMailSender.send(mailMessage);

        return "redirect:/";
    }

}
