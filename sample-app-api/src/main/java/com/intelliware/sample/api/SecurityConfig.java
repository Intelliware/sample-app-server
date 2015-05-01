package com.intelliware.sample.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.service.SampleUserDetailsService;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().authenticationEntryPoint(getAuthenticationEntryPoint())
				.and().authorizeRequests().anyRequest().authenticated()
				.and().logout()
				.and().csrf().disable();
	}
	
	@Bean
	public AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

    @Autowired
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder( new BCryptPasswordEncoder());
    }

	@Override
    protected UserDetailsService userDetailsService() {
		return new SampleUserDetailsService(userRepository);
	}
}