package com.rost.springsecurity1.config;

import com.rost.springsecurity1.service.PersonDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

 private final PersonDetailService personDetailService;

 private final JWTFilter filter;
    @Autowired
    public SecurityConfig(PersonDetailService personDetailService, JWTFilter filter) {
        this.personDetailService = personDetailService;
        this.filter = filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/auth/login","/auth/registration" ,"/error").permitAll() //сюда пускаем всех
                .anyRequest().hasAnyRole("USER","ADMIN")


                     .and() // для разграничения функционала авторизации и аутентификации
                .formLogin().loginPage("/auth/login")  //направляем всех на страницу аутентификации
                .loginProcessingUrl("/process_login")  // здесь security будет ждать логин/пароль (из формы)
                .defaultSuccessUrl("/hello",true)  //после успеха всех перенаправляет
                .failureUrl("/auth/login?error")// при некоррект вводе возвращает
                     .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                     .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    // Настраиваем Аутентификацию
    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(personDetailService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
       return new BCryptPasswordEncoder();
    }
}

