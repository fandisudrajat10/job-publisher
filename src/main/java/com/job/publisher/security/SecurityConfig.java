package com.job.publisher.security;

import com.job.publisher.config.APIKeyProperties;
import com.job.publisher.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private APIKeyProperties apiKeyProperties;

    private static final String EMPLOYER = "EMPLOYER";
    private static final String FREELANCER = "FREELANCER";
    private static final String USER = "USER";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new TokenFilter(jwtTokenProvider, apiKeyProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/job").hasAuthority(EMPLOYER)
                .antMatchers(HttpMethod.GET, "/api/v1/job/list").hasAnyAuthority(FREELANCER, EMPLOYER)
                .antMatchers(HttpMethod.PATCH, "/api/v1/job/publish/{jobId}").hasAuthority(EMPLOYER)
                .antMatchers(HttpMethod.POST, "/api/v1/proposal").hasAuthority(FREELANCER)
                .antMatchers(HttpMethod.GET, "/api/v1/proposal/my-proposals").hasAuthority(FREELANCER)
                .antMatchers(HttpMethod.GET, "/api/v1/proposal/my-jobs").hasAuthority(EMPLOYER)
                .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/users").hasAuthority(USER)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"errors\":\"" + authException.getMessage() + "\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"errors\":\"" + accessDeniedException.getMessage() + "\"}");
                })
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}