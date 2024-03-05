package com.guardjo.feedbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.guardjo.feedbook.config.auth.CustomAuthEntryPoint;
import com.guardjo.feedbook.config.auth.JwtAuthManager;
import com.guardjo.feedbook.config.auth.JwtFilter;
import com.guardjo.feedbook.controller.UrlContext;
import com.guardjo.feedbook.util.JwtProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthManager jwtAuthManager, JwtProvider jwtProvider) throws
		Exception {
		httpSecurity.authorizeHttpRequests(registry -> {
				registry.requestMatchers(UrlContext.LOGIN_URL, UrlContext.SIGNUP_URL)
					.permitAll()
					.anyRequest().authenticated();
			})
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtFilter(jwtAuthManager, jwtProvider), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(configure -> {
				configure.authenticationEntryPoint(new CustomAuthEntryPoint());
			});

		return httpSecurity.build();
	}
}
