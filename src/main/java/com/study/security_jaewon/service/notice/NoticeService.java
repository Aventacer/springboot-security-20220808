package com.study.security_jaewon.service.notice;

import java.util.List;

import com.study.security_jaewon.web.dto.notice.GetNoticeListResponseDto;
import com.study.security_jaewon.web.dto.notice.GetNoticeResponseDto;
import com.study.security_jaewon.web.dto.notice.addNoticeReqDto;

public interface NoticeService {
	
	public List<GetNoticeListResponseDto> getNoticeList(int page, String searchFlag, String searchValue) throws Exception;
	public int addNotice(addNoticeReqDto addNoticeReqDto) throws Exception;
	public GetNoticeResponseDto getNotice(String flag, int noticeCode) throws Exception;
}
