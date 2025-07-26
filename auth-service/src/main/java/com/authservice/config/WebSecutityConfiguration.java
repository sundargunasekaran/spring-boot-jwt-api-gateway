package com.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authservice.filters.JWTAuthenticationEntryPoint;
import com.authservice.filters.JWTFilter;
import com.authservice.utils.AppAuthenticationProvider;



/**
 * @author Sundar G
 * Date: 26/04/2021
 * Time: 10:47 AM
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class WebSecutityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AppAuthenticationProvider authenticationProvider;
	
	@Autowired
	private JWTFilter jwtFilter;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthEntryPoint;
    
    /* @Autowired
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }*/

	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	}

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
               // .antMatchers("/**").permitAll()
                .antMatchers("/authapi/greet").permitAll()
                .antMatchers("/authapi/login").permitAll()
               // .antMatchers("/authapi/refreshToken").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and()
            //    .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      // Allow swagger to be accessed without authentication
      web.ignoring().antMatchers("/v2/api-docs")//
          .antMatchers("/swagger-resources/**")//
          .antMatchers("/swagger-ui.html")//
          .antMatchers("/configuration/**")//
          .antMatchers("/webjars/**")//
          .antMatchers("/public")
          
          // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
          .and()
          .ignoring()
          .antMatchers("/h2-console/**/**");
    }

}
