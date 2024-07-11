package com.steven.curso.springboot.app.springboot_crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.steven.curso.springboot.app.springboot_crud.security.filter.JwtAuthenticationFilter;
import com.steven.curso.springboot.app.springboot_crud.security.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests((authz) -> authz
        .requestMatchers(HttpMethod.GET, "api/users").permitAll() // la ruta api/users queda publica solo para metodos GET, listar usuarios
        .requestMatchers(HttpMethod.POST, "api/users/register").permitAll() // la ruta api/users/register queda publica solo en POST, crear usuarios NO ADMIN
        .requestMatchers(HttpMethod.POST, "api/users").hasRole("ADMIN") // la ruta api/users queda restringida solo para el ADMIN, solo el ADMIN puede gestionar crear otros usuarios y admins
        .requestMatchers(HttpMethod.GET, "api/products", "/api/products/{id}").hasAnyRole("ADMIN","USER") // la ruta api/products queda restringida para los roles ADMIN Y USER, solo el ADMIN y USER pueden ver el detalle de los productos
        .requestMatchers(HttpMethod.POST, "api/products").hasRole("ADMIN") // la ruta api/products queda restringida solo para el ADMIN, solo el ADMIN puede crear los productos
        .requestMatchers(HttpMethod.PUT, "api/products/{id}").hasRole("ADMIN") // la ruta api/products/{id} queda restringida solo para el ADMIN, solo el ADMIN puede modificar los productos
        .requestMatchers(HttpMethod.DELETE, "api/products/{id}").hasRole("ADMIN") // la ruta api/products/{id} queda restringida solo para el ADMIN, solo el ADMIN puede eliminar los productos
        .anyRequest().authenticated()) // el resto de rutas necesitan autenticacion
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable()) // deshabilitamos el token csrf para evitar vulnerabilidades, hack en formularios
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //la session http no queda guardada, queda sin estado. el usuario debe autenticarse cada vez que hace una peticion http
        .build();
    }
    
}
