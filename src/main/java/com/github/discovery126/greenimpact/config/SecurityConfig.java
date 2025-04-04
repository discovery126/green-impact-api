package com.github.discovery126.greenimpact.config;

import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.RoleHeaderFilter;
import com.github.discovery126.greenimpact.service.CustomDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RoleHeaderFilter roleHeaderFilter)
            throws Exception {
       http
               .cors(corsConfigurer -> corsConfigurer
                       .configurationSource(request -> {
                           CorsConfiguration config = new CorsConfiguration();
                           config.setAllowedOrigins(List.of("http://localhost:5173"));
                           config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                           config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                           config.setExposedHeaders(List.of("Authorization","X-User-Roles"));
                           return config;
                       }))
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests((auth) ->
                       auth.requestMatchers("/v1/register",
                                       "/v1/user/tasks",
                                       "/v1/events",
                                       "/v1/rewards",
                                       "/error").permitAll()
                               .anyRequest().authenticated())
               .httpBasic(withDefaults())
               .formLogin(AbstractHttpConfigurer::disable);

       http.addFilterAfter(roleHeaderFilter,
               org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.class);

       return http.build();
    }
}
