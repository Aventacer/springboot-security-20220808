package com.study.security_jaewon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.study.security_jaewon.config.auth.AuthFailureHandler;

@EnableWebSecurity //기존의 WebSecurityConfigurerAdapter를 비활성 시키고 현재 시큐리티 설정을 따르겠다.
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 토큰해제
		http.csrf().disable();
		// 요청이 들어왔을때 인증
		http.authorizeRequests()
			.antMatchers("/","/index","/mypage/**")				// 우리가 지정한 요청
			.authenticated()						// 인증을 거쳐라
			.anyRequest()							// 다른 모든 요청
			.permitAll()							// 모드 권한
			.and()									// 이전까지 한 세트 (묶음)
			.formLogin()							// httpbasicLogin, formLogin, jwtLogin 중 fomeLogin을 사용한다
			.loginPage("/auth/signin")				// 로그인 페이지 즉 요청 주소 Get 요청으로 접근한다
			.loginProcessingUrl("/auth/signin")		// 로그인 요청(post요청)
			.failureHandler(new AuthFailureHandler())
			.defaultSuccessUrl("/");				// 바로 로그인화면으로 들어올때 로그인성공하고 get요청되어 들어가는 url
	}
}
