package pe.edu.galaxy.training.parqueaderov1.controller.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {


  private UserDetailsService userDetailsService;

  public WebSecurity(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    /*
     * 1. Se desactiva el uso de cookies
     * 2. Se activa la configuración CORS con los valores por defecto
     * 3. Se desactiva el filtro CSRF
     * 4. Se indica que el login no requiere autenticación
     * 5. Se indica que el resto de URLs esten aseguradas
     */
    httpSecurity
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
       .and()
       .cors()
       .and()
       .csrf()
       .disable()
       .authorizeRequests()
       .antMatchers(HttpMethod.POST, Constants.LOGIN_URL).permitAll()
       //.antMatchers(HttpMethod.GET, "/v1/usuario/**").permitAll()
            .antMatchers(HttpMethod.POST, "/v1/usuario/**").permitAll()
       .anyRequest()
       .authenticated()
       .and()
        .addFilter(new JWTAuthenticationFilter(authenticationManager())) // Login
        .addFilter(new JWTAuthorizationFilter(authenticationManager())); // Recurso/Api/Metodos
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
     // se ejecuta al correr el sistema
    // Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
    System.out.println("configure...");
   // auth.inMemoryAuthentication();
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }
/*
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:4200");
    config.addAllowedHeader("*");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
  /*

 */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
    corsConfig.addAllowedMethod(HttpMethod.DELETE);
    corsConfig.addAllowedMethod(HttpMethod.PUT);
    /*
    configuration.setAllowedOrigins(asList("*"));
    configuration.setAllowedMethods(asList("HEAD","GET", "POST", "PUT", "DELETE"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
    source.registerCorsConfiguration("/**", configuration);
    */
    source.registerCorsConfiguration("/**", corsConfig);
    return source;
  }
}