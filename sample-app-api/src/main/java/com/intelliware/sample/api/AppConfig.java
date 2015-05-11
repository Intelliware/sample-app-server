package com.intelliware.sample.api;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.service.SampleUserDetailsService;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig extends WebSecurityConfigurerAdapter {
	
	private static final List<String> ACCEPED_METHODS = Arrays.asList("put", "post");
	
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
	
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver() {
			@Override
			public boolean isMultipart(HttpServletRequest request) {
				String method = request.getMethod().toLowerCase();
				// By default, only POST is allowed. Since this is an 'update' we should accept PUT.
				if (!ACCEPED_METHODS.contains(method)) {
					return false;
				}
				String contentType = request.getContentType();
				return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
			}
		};
	}
}