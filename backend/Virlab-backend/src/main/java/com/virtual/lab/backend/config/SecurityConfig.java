package com.virtual.lab.backend.config;

import com.virtual.lab.backend.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers( "/api/login").permitAll()
                        .requestMatchers("/api/user/register").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/client/**").hasRole("CLIENT")
                        .requestMatchers("/api/technician/**").hasRole("TECHNICIEN")
                        .requestMatchers("/api/tests/**").hasAnyRole("TECHNICIEN", "CLIENT", "ADMIN")
                        .requestMatchers("/api/results/**").hasAnyRole("TECHNICIEN", "ADMIN")
                        .requestMatchers("/api/test-types/**").hasRole("ADMIN")
                        .requestMatchers("/api/mesures/**").hasRole("ADMIN")
                        .requestMatchers("/api/mesures/bytesttype/**").hasRole("ADMIN")
                        .requestMatchers("/api/products/mes").hasRole("CLIENT")
                        .requestMatchers("/api/products/join/**", "/api/products/qrcode/{accessCode}").hasRole("CLIENT")
                        .requestMatchers("/api/products/project/**").hasAnyRole("ADMIN","CLIENT", "TECHNICIEN")
                        .requestMatchers("/api/products/technician/**").hasRole("TECHNICIEN")
                        .requestMatchers("/api/products/client/getAll").hasRole("CLIENT")
                        .requestMatchers("/api/products/client/**").hasRole("CLIENT")
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/messages").hasAnyRole("ADMIN","CLIENT", "TECHNICIEN")
                        .requestMatchers("/api/messages/product/**", "/api/messages/conversation", "/api/messages/unread/**", "/api/messages/{messageId}/read"
                        ).hasAnyRole("CLIENT", "TECHNICIEN", "ADMIN")
                        .requestMatchers("/ws/**", "/topic/**").permitAll()
                        .requestMatchers("/api/id/**").permitAll()
                        .requestMatchers("/api/files/upload").hasAnyRole("ADMIN","CLIENT", "TECHNICIEN")
                        .requestMatchers("/api/files/download/**").authenticated()
                        .requestMatchers("/api/files/user/**", "/api/files/client/**",  "/api/files/products/**").authenticated()
                        .requestMatchers("/api/files/all").hasRole("ADMIN")
                        .requestMatchers("/api/test-groups/createGroup", "/api/test-groups/updateGroup/{id}, /api/test-groups/deleteGroup/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/test-groups/**").authenticated()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // ← Utilisez le même partout
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // ← Utilisez le bean
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}