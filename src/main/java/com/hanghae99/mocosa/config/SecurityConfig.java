package com.hanghae99.mocosa.config;

//import com.hanghae99.mocosa.config.jwt.CustomAuthenticationEntryPoint;
import com.hanghae99.mocosa.config.jwt.JwtAuthenticationFilter;
import com.hanghae99.mocosa.config.jwt.JwtAuthorizationFilter;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
                .authorizeRequests()
                .antMatchers("/signup","/signin","/login","/","/search").permitAll()
                .antMatchers("/images/**","/css/**","basic.js").permitAll()

                .antMatchers(HttpMethod.GET, "/products/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers("/notification","/users/orders")
                .access("hasRole('ROLE_USER')")

                .antMatchers(HttpMethod.POST,"/products/**")
                .access("hasRole('ROLE_USER')")

                .antMatchers("/users/restock")
                .access("hasRole('ROLE_ADMIN')")

                .anyRequest().authenticated();

//        http
//                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }
}
