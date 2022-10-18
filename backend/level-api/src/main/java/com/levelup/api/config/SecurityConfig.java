package com.levelup.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelup.api.filter.*;
import com.levelup.member.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true) //secured 애노테이션 활성화
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() //cors는 따로 설정했으니 기본 설정
                .and()
                .formLogin().disable()
                .csrf().disable()
                .httpBasic().disable() // token 방식을 사용하니 basic은 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// session 기반이 아님을 선언
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //인증 실패시
                .accessDeniedHandler(jwtAccessDeniedHandler); //권한 에러 처리(관리자 권한 등)

        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()

                .antMatchers(POST,"/api/v1/sign-up", "/api/v1/members/image", "/api/v1/login").permitAll()
                .antMatchers("/api/v1/members").authenticated()

                .antMatchers(GET, "/api/v1/channels/**").permitAll()
                .antMatchers("/api/v1/channels/{\\d+}/manager", "/api/v1/channels/{\\d+}/member/**",
                        "/api/v1/channels/{\\d+}/waiting-member/**").hasAnyRole("CHANNEL_MANAGER", "ADMIN")
                .antMatchers("/api/v1/channels/**").hasAnyRole("MEMBER", "CHANNEL_MANAGER", "ADMIN")

                .antMatchers(GET, "/api/v1/comments/**").permitAll()
                .antMatchers("/api/v1/comments/**").hasAnyRole("MEMBER", "CHANNEL_MANAGER", "ADMIN")

                .antMatchers(GET, "/api/v1/channel-posts/**").permitAll()
                .antMatchers("/api/v1/channel-posts/**").hasAnyRole("MEMBER", "CHANNEL_MANAGER", "ADMIN")

                .antMatchers(GET, "/api/v1/articles/**").permitAll()
                .antMatchers("/api/v1/articles/**").hasAnyRole("MEMBER", "CHANNEL_MANAGER", "ADMIN")

                .antMatchers("/api/v1/votes/**").authenticated();
        http
                .addFilterAfter(getSecurityLoginFilter(), CorsFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, getSecurityLoginFilter().getClass());
    }

    private LoginFilter getSecurityLoginFilter() throws Exception {
        LoginFilter securityLoginFilter = new LoginFilter(objectMapper, new TokenProvider());

        securityLoginFilter.setAuthenticationManager(authenticationManager());
        securityLoginFilter.setFilterProcessesUrl("/api/v1/login");
        securityLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return securityLoginFilter;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**");
    }
}
