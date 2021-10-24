package com.blackship.greenion.user;

import com.blackship.greenion.user.oauth2.CustomOauth2UserService;
import com.blackship.greenion.user.oauth2.JwtAuthFilter;
import com.blackship.greenion.user.oauth2.OAuth2SuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOauth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;

    public UserSecurityConfig(CustomOauth2UserService customOAuth2UserService, OAuth2SuccessHandler successHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers("/token/**").permitAll()
                        .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                    .successHandler(successHandler)
                    .userInfoEndpoint().userService(customOAuth2UserService);

        http.addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}