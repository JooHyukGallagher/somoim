package me.weekbelt.runningflex.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 패턴을 이용해서 접근을 제한
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up",
                        "/check-email-token", "login-by-email", "/email-login", "/check-email-login",
                        "/login-link").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .anyRequest().authenticated();
    }

    // 글로벌 보안에 영향(자원 무시, 디버그 모드 설정, 사용자 정의 방화벽 정의를 구현하여 요청 거부)
    // static 관련 리소스들은 스프링 시큐리티 적용 x
    @Override
    public void configure(WebSecurity web) throws Exception {
        // SpringSecurity가 무시해야하는 RequestMatcher 인스턴스를 추가
        web.ignoring()
                .mvcMatchers("account/email-login")
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
