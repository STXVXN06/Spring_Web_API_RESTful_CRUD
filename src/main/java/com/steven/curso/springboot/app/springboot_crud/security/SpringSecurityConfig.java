package com.steven.curso.springboot.app.springboot_crud.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.steven.curso.springboot.app.springboot_crud.entities.Role;
import com.steven.curso.springboot.app.springboot_crud.entities.Route;
import com.steven.curso.springboot.app.springboot_crud.repositories.RouteRepository;
import com.steven.curso.springboot.app.springboot_crud.security.filter.JwtAuthenticationFilter;
import com.steven.curso.springboot.app.springboot_crud.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private RouteRepository routeRepository;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean // Curso
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    //     return http.authorizeHttpRequests((authz) -> authz
    //     .requestMatchers(HttpMethod.GET, "api/users").permitAll() // la ruta api/users queda publica solo para metodos GET, listar usuarios
    //     .requestMatchers(HttpMethod.POST, "api/users/register").permitAll() // la ruta api/users/register queda publica solo en POST, crear usuarios NO ADMIN
    //     .requestMatchers(HttpMethod.POST, "api/users").hasRole("ADMIN") // la ruta api/users queda restringida solo para el ADMIN, solo el ADMIN puede gestionar crear otros usuarios y admins
    //     .requestMatchers(HttpMethod.GET, "api/products", "/api/products/{id}").hasAnyRole("ADMIN","USER") // la ruta api/products queda restringida para los roles ADMIN Y USER, solo el ADMIN y USER pueden ver el detalle de los productos
    //     .requestMatchers(HttpMethod.POST, "api/products").hasRole("ADMIN") // la ruta api/products queda restringida solo para el ADMIN, solo el ADMIN puede crear los productos
    //     .requestMatchers(HttpMethod.PUT, "api/products/{id}").hasRole("ADMIN") // la ruta api/products/{id} queda restringida solo para el ADMIN, solo el ADMIN puede modificar los productos
    //     .requestMatchers(HttpMethod.DELETE, "api/products/{id}").hasRole("ADMIN") // la ruta api/products/{id} queda restringida solo para el ADMIN, solo el ADMIN puede eliminar los productos
    //     .anyRequest().authenticated()) // el resto de rutas necesitan autenticacion
    //     .addFilter(new JwtAuthenticationFilter(authenticationManager()))
    //     .addFilter(new JwtValidationFilter(authenticationManager()))
    //     .csrf(config -> config.disable()) // deshabilitamos el token csrf para evitar vulnerabilidades, hack en formularios
    //     .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //     la session http no queda guardada, queda sin estado. el usuario debe autenticarse cada vez que hace una peticion http
    //     .build();
    // }


    @Bean // Mio
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(authz -> {

            List<Route> routes = (List<Route>) routeRepository.findAllWithRoles();
            for (Route route : routes) {
                if (route.getRoles().isEmpty()) {
                    authz.requestMatchers(HttpMethod.valueOf(route.getMethod()), route.getPath())
                    .permitAll();
                }else{
                    String[] roles = route.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .toArray(String[]::new);
                    authz.requestMatchers(HttpMethod.valueOf(route.getMethod()), route.getPath())
                        .hasAnyRole(roles);

                }
            }
        authz.anyRequest().authenticated();
        }) 
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable()) // deshabilitamos el token csrf para evitar vulnerabilidades, hack en formularios
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // la session http no queda guardada, queda sin estado. el usuario debe autenticarse cada vez que hace una peticion http
        .build();
    }

    // @Bean // ChatGpt
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf(csrf -> csrf.disable())
    //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .authorizeHttpRequests(authz -> {
    //             // Cargar las rutas y sus roles desde la base de datos
    //             List<Route> routes = (List<Route>) routeRepository.findAllWithRoles();
    //             for (Route route : routes) {
    //                 System.out.println(route.getRoles());
    //             }
    //             for (Route route : routes) {
    //                 if (route.getRoles().isEmpty()) {
    //                     authz.requestMatchers(HttpMethod.valueOf(route.getMethod())).permitAll();
    //                 }else{
    //                     String[] roles = route.getRoles().stream()
    //                         .map(role -> role.getName().replace("ROLE_", ""))
    //                         .toArray(String[]::new);
    //                     authz.requestMatchers(HttpMethod.valueOf(route.getMethod()), route.getPath())
    //                         .hasAnyRole(roles);

    //                 }
    //             }
    //             authz.anyRequest().authenticated();
    //         })
    //         .addFilter(new JwtAuthenticationFilter(authenticationManager()))
    //         .addFilter(new JwtValidationFilter(authenticationManager()));
        
    //     return http.build();
    // }
    
}
