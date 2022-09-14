package com.study.security_jaewon.web.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.security_jaewon.service.notice.NoticeService;
import com.study.security_jaewon.web.dto.CMRespDto;
import com.study.security_jaewon.web.dto.notice.GetNoticeListResponseDto;
import com.study.security_jaewon.web.dto.notice.GetNoticeResponseDto;
import com.study.security_jaewon.web.dto.notice.addNoticeReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notice")
@Slf4j
@RequiredArgsConstructor
public class NoticeRestController {
	
//	@Value("${file.path}")
//	private String filePath;
	
	@Value("${file.path}")
	private String filePath;
	
	private final NoticeService noticeService;
	
	@GetMapping("/list/{page}")
	public ResponseEntity<?> getNoticeList(@PathVariable int page, @RequestParam String searchFlag , @RequestParam String searchValue) {
		List<GetNoticeListResponseDto> listDto = null;
		
		try {
			listDto = noticeService.getNoticeList(page, searchFlag, searchValue);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1,"database error", listDto));
		}
		return ResponseEntity.ok(new CMRespDto<>(1,"lookup successful", listDto));
	}
	
	@PostMapping("")
	public ResponseEntity<?> addNotice(addNoticeReqDto addNoticeReqDto) {
		log.info(">>>>> {}", addNoticeReqDto);
		log.info(">>>>> fileName: {}", addNoticeReqDto.getFile().get(0).getOriginalFilename());
		//log.info("filePath: {}", filePath);
		
		int noticeCode = 0;
		
		try {
			noticeCode = noticeService.addNotice(addNoticeReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Failed to write", noticeCode));
		}
		
		return ResponseEntity.ok(new CMRespDto<>(1, "completing creation", noticeCode));
	}
	
	@GetMapping("/{noticeCode}")
	public ResponseEntity<?> getNotice(@PathVariable int noticeCode)  {
		GetNoticeResponseDto getNoticeResponseDto = null;
		try {
			getNoticeResponseDto = noticeService.getNotice(null, noticeCode);
			if(getNoticeResponseDto == null) {
				return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "1request failed", null));				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "database error", null));				
		}
		return ResponseEntity.ok().body(new CMRespDto<>(1, "lookup successful", getNoticeResponseDto));				
	}
	
	@GetMapping("/{flag}/{noticeCode}") // 이전, 이후 게시글
	public ResponseEntity<?> getNotice(@PathVariable String flag, @PathVariable int noticeCode)  {
		GetNoticeResponseDto getNoticeResponseDto = null;
		if(flag.equals("pre") || flag.equals("next")) {
			try {
				getNoticeResponseDto = noticeService.getNotice(flag, noticeCode);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "database error", null));				
			}
			
		}else {
			return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "2request failed", null));
		}
		return ResponseEntity.ok().body(new CMRespDto<>(1, "lookup successful", getNoticeResponseDto));				
	}
	
	@GetMapping("/file/download/{fileName}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
		Path path = Paths.get(filePath + "notice/" + fileName);
		
		String contentType = Files.probeContentType(path);
		log.info("contentType: {}" + contentType);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment")
														.filename(fileName, StandardCharsets.UTF_8)
														.build());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		
		return ResponseEntity.ok().headers(headers).body(resource);
	}
}
