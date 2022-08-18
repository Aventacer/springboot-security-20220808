package com.study.security_jaewon.domain.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
	
	public int save(User user) throws Exception;	// 회원가입
	public User findUserByUsername(String username) throws Exception;	// 로그인 시에
	public User findOAuth2UserByUsername(String oauth2_id) throws Exception;
}
