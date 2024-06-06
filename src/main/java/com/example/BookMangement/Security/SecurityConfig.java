package com.example.BookMangement.Security;


import com.example.BookMangement.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {


    private UserDetailsService userDetailsService;
    private final UserService userService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // .requestMatchers("/**").permitAll()
                                .requestMatchers("/").hasAnyAuthority("ADMIN","EMPLOYEE")
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/employee/**").hasAuthority("ADMIN")
                                .requestMatchers("/author/**").hasAuthority("ADMIN")
                                .requestMatchers("/book-category/**").hasAuthority("ADMIN")
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers("/book/").hasAuthority("ADMIN")
                                .requestMatchers("/send-request/").hasAuthority("ADMIN")

                                .anyRequest().authenticated()



                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .successHandler(loginSuccessHandler)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                       .exceptionHandling(exceptionHandling ->
                               exceptionHandling.accessDeniedPage("/403"));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}