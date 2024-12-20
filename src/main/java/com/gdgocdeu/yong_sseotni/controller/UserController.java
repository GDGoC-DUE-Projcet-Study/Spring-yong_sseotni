package com.gdgocdeu.yong_sseotni.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdgocdeu.yong_sseotni.service.UserService;
import com.gdgocdeu.yong_sseotni.vo.User;

@RestController
@CrossOrigin()
@RequestMapping(value="api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	// MD5 암호화 메서드
	private String encryptMD5(String password) {
	    return DigestUtils.md5DigestAsHex(password.getBytes());
	}
	
	// 회원 비밀번호 변경
	@PostMapping("updatePw")
	public ResponseEntity<String> updatePw(
			@RequestParam(value="user_idx") int user_idx,
			@RequestParam(value="current_pw") String current_pw,
			@RequestParam(value="new_pw") String new_pw
			) {
		
		User user = userService.findByIdx(user_idx);
		
		if (user == null) {
			return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
		}
		
		String currentPw = encryptMD5(current_pw);
		if (!user.getUser_pw().equals(currentPw)) {
			return new ResponseEntity<String>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
		}
		
		String newPw = encryptMD5(new_pw);
		if (currentPw.equals(newPw)) {
			return new ResponseEntity<String>("현재 비밀번호와 동일합니다. 다른 비밀번호를 입력해주세요.", HttpStatus.FORBIDDEN);
		}
		
		user.setUser_pw(newPw);
		userService.updatePw(user);
		
		return new ResponseEntity<String>("비밀번호가 변경되었습니다.", HttpStatus.OK);
		
	}
	
	// 회원탈퇴
	@PostMapping("deleteUser")
	public ResponseEntity<String> deleteUser(
			@RequestParam(value="user_idx") int user_idx,
			@RequestParam(value="user_pw") String user_pw
			) {
		
		User user = userService.findByIdx(user_idx);
		
		if (user == null) {
	        return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
	    }
		
		String currentPw = user.getUser_pw();
		if (user.getUser_pw() == null || !user.getUser_pw().equals(currentPw)) {
			return new ResponseEntity<String>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
		}
		
		user.setDel_ny("y");
		userService.deleteUser(user);
		
		return new ResponseEntity<String>("회원탈퇴가 완료되었습니다.", HttpStatus.OK);
		
	}
	
	// 회원정보 수정
	@PostMapping("updateUser")
	public ResponseEntity<?> updateUser(
			@RequestParam(value="user_idx") int user_idx,
			@RequestParam(value="user_nick") String user_nick,
			@RequestParam(value="user_birth") String user_birth
			) {
		
		User user = userService.findByIdx(user_idx);
		
		if (user == null) {
	        return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
	    }
		
		user.setUser_nick(user_nick);
		user.setUser_birth(user_birth);
		
		userService.updateUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	}
	
	// 로그인
	@PostMapping("login")
	public ResponseEntity<Object> login(
	            @RequestParam(value="user_email") String user_email,
	            @RequestParam(value="user_pw") String user_pw,
	            HttpSession session
	        ){
	    
	    User u = new User();
	    u.setUser_email(user_email);
	    u.setUser_pw(user_pw);
	    
	    User result = userService.login(u);
	    
	    if(result != null) {
	        // 세션에 정보 넣기
	        session.setAttribute("me", result);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } else {
	        // 로그인 실패 시 적절한 에러 메시지와 상태 코드 반환
	        return new ResponseEntity<>("이메일 또는 비밀번호가 잘못 되었습니다. 이메일과 비밀번호를 정확히 입력해 주세요.", HttpStatus.UNAUTHORIZED);
	    }
	}

	
	// 회원가입
	@PostMapping("join")
	public ResponseEntity<?> join (
			@RequestParam(value="login_provider") String login_provider,
			@RequestParam(value="user_email") String user_email,
			@RequestParam(value="user_pw") String user_pw,
			@RequestParam(value="user_nick") String user_nick,
			@RequestParam(value="user_birth") String user_birth
			) {
		
		// 이메일 유효성 검사
		User ue = userService.findByEmail(user_email);
		
		if (ue != null) {
			return new ResponseEntity<String>("이미 가입된 회원입니다.", HttpStatus.FORBIDDEN);
		}
		
		if (login_provider.isEmpty()) {
			login_provider= "basic";
		}
		
		
		// 회원가입
		User u = new User();
		
		u.setLogin_provider(login_provider);
		u.setUser_email(user_email);
		u.setUser_pw(user_pw);
		u.setUser_nick(user_nick);
		u.setUser_birth(user_birth);
		
		
		userService.join(u);
		
		User result = userService.findByEmail(user_email);
		return new ResponseEntity<User>(result, HttpStatus.OK);
		
	}
	
}
