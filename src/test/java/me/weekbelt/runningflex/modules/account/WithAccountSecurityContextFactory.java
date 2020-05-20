package me.weekbelt.runningflex.modules.account;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.weekbelt.runningflex.modules.account.service.AccountService;
import me.weekbelt.runningflex.modules.account.form.SignUpForm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @SneakyThrows
    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String nickname = withAccount.value();

        // 회원 가입
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname(nickname);
        signUpForm.setEmail(nickname + "@gmail.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);

        // Authentication 만들고 SecurityContext에 넣기
        UserDetails principal = accountService.loadUserByUsername(nickname);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
