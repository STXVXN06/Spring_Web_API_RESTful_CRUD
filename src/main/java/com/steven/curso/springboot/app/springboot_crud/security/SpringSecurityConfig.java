package com.steven.curso.springboot.app.springboot_crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests((authz) -> authz
        .requestMatchers(HttpMethod.GET, "api/users").permitAll() // la ruta api/users queda publica solo para metodos GET
        .requestMatchers(HttpMethod.POST, "api/users/register").permitAll() // la ruta api/users/register queda publica solo en POST
        .anyRequest().authenticated()) // el resto de rutas necesitan autenticacion
        .csrf(config -> config.disable()) // deshabilitamos el token csrf para evitar vulnerabilidades, hack en formularios
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //la session http no queda guardada, queda sin estado. el usuario debe autenticarse cada vez que hace una peticion http
        .build();
    }
    
}
