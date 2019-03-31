package com.example.Clinic;

import com.example.Clinic.models.PatientDetail;
import com.example.Clinic.models.handlers.SuccessLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final
    SuccessLoginHandler loginHandler;
    final
    PatientDetail patientDetail;

    @Autowired
    public SecurityConfig(SuccessLoginHandler loginHandler, PatientDetail patientDetail) {
        this.loginHandler = loginHandler;
        this.patientDetail = patientDetail;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/patientZone/**")
                .authenticated().and()
                .formLogin()
                .loginPage("/patientPanel").permitAll()
                .usernameParameter("login")
                .passwordParameter("password")
                .successHandler(loginHandler);



       // http.authorizeRequests()
            //    .antMatchers("/patient")
            //    .authenticated()
            //    .antMatchers("/")
            //    .permitAll();
                //.and()
                //.formLogin()
                //.loginPage("/login")
                //.usernameParameter("login")
                //.passwordParameter("password")
                //.defaultSuccessUrl("/",true);
                //.successHandler(loginHandler)
                //.and()
                //.exceptionHandling()
                //.accessDeniedPage("/permissionDenied");
                //.and()
                //.csrf().disable();
    }

    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        ShaPasswordEncoder encoder=new ShaPasswordEncoder();                 //szyfrowanie hasla
        auth.userDetailsService(patientDetail).passwordEncoder(encoder);
    }
}
