package com.study.security_jaewon.web.dto.notice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class addNoticeReqDto {
	private String noticeTitle;
	private int userCode;
	private String ir1;
	private List<MultipartFile> file;
}
