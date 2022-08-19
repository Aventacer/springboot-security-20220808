package com.study.security_jaewon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import com.study.security_jaewon.config.auth.AuthFailureHandler;
import com.study.security_jaewon.service.auth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity //기존의 WebSecurityConfigurerAdapter를 비활성 시키고 현재 시큐리티 설정을 따르겠다.
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final CorsFilter corsFilter;
	private final PrincipalOauth2UserService principalOauth2UserService;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 토큰해제
		http.csrf().disable();
		http.headers()
			.frameOptions()
			.disable();
		http.addFilter(corsFilter);					// Cors 인증을 하지 않겠다.
		// 요청이 들어왔을때 인증
		http.authorizeRequests()
			.antMatchers("/api/v1/grant/test/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			
			.antMatchers("/api/v1/grant/test/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			
			.antMatchers("/api/v1/grant/test/admin/**")
			.access("hasRole('ROLE_ADMIN')")
			
			.antMatchers("/","/index","/mypage/**")		// 우리가 지정한 요청
			.authenticated()							// 인증을 거쳐라
			
			.anyRequest()								// 다른 모든 요청
			.permitAll()								// 모드 권한
			
			.and()										// 이전까지 한 세트 (묶음)
			
			.formLogin()								// httpbasicLogin, formLogin, jwtLogin 중 fomeLogin을 사용한다
			.loginPage("/auth/signin")					// 로그인 페이지 즉 요청 주소 Get 요청으로 접근한다
			.loginProcessingUrl("/auth/signin")			// 로그인 요청(post요청)
			.failureHandler(new AuthFailureHandler())
			//.defaultSuccessUrl("/")					// 바로 로그인화면으로 들어올때 로그인성공하고 get요청되어 들어가는 url
			
			.and()
			
			.oauth2Login()								//
			.userInfoEndpoint()							// oauth2Login 할때 반드시 써야하는것, yml에 설정한걸 실행해줌
			/*
			 * 1. google, naver, kakao 등 로그인 요청 -> 코드를 발급해줌.
			 * 2. 발급받은 코드를 가진 상태로 권한 요청(토큰발급요청)을 함.
			 * 3. 스코프에 등록된 프로필 정보를 가져올 수 있게된다.
			 * 4. 해당 정보를 Security의 객체로 전달받음.
			 */
			.userService(principalOauth2UserService)	// 
			
			.and()
			//.failureHandler(null)						// login fail
			
			.defaultSuccessUrl("/index")
			;
	}
}
