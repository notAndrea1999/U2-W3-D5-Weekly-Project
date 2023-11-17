package andreademasi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    AuthFilter authFilter;
    @Autowired
    ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http.formLogin(login -> login.disable());

        //Aggiungo il filtro custom
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        // Secondo filtro custom
        http.addFilterBefore(exceptionHandlerFilter, AuthFilter.class);

        http.authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll());
        return http.build();
    }

}



