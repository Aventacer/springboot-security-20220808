package com.study.security_jaewon.service.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.security_jaewon.domain.user.User;
import com.study.security_jaewon.domain.user.UserRepository;
import com.study.security_jaewon.web.dto.auth.SignupReqDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//System.out.println("username: " + username);
		
		User userEntity = null;
		
		try {
			userEntity = userRepository.findUserByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException(username);
		}
		
		if(userEntity == null) {
			throw new UsernameNotFoundException(username + "사용자이름은 사용할 수 없습니다.");
		}
				
		return new PrincipalDetails(userEntity);
	}
	
	public boolean addUser(SignupReqDto signupReqDto) throws Exception{
//		User user = User.builder()
//				.user_name("유재원")
//				.user_email("jaewun1@gmail.com")
//				.user_id("asdf")
//				.user_password(new BCryptPasswordEncoder().encode("1234"))
//				.user_roles("ROLE_USER, ROLE_MANAGER")
//				.build();
		
		
		
		return userRepository.save(signupReqDto.toEntity()) > 0;
	}
	
}
