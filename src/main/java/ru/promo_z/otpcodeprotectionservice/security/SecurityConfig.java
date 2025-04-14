package ru.promo_z.otpcodeprotectionservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final List<String> ALLOWED_USER_ROLE_URL_PATTERNS = List.of("/otp-codes/validation", "/otp-codes");
    private static final List<String> ALLOWED_ADMIN_ROLE_URL_PATTERNS = List.of("/otp-codes/**", "/users", "/users/**");
    private static final List<String> ALLOWED_URL_PATTERNS = List.of("/error", "/auth/registration", "/auth/login");


    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter,
                          AuthenticationEntryPoint authenticationEntryPoint) {

        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                        auth -> auth.requestMatchers(ALLOWED_URL_PATTERNS
                                        .toArray(String[]::new)).permitAll()
                                .requestMatchers("/users/test").permitAll()
                                .requestMatchers(ALLOWED_USER_ROLE_URL_PATTERNS.toArray(String[]::new))
                                .hasAnyRole("USER", "ADMIN")
                                .requestMatchers(ALLOWED_ADMIN_ROLE_URL_PATTERNS.toArray(String[]::new)).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(configure ->
                        configure.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
