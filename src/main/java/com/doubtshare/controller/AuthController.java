package com.doubtshare.controller;

import com.doubtshare.dto.LoginRequest;
import com.doubtshare.dto.LoginResponse;
import com.doubtshare.dto.RegisterRequest;
import com.doubtshare.dto.UserDTO;
import com.doubtshare.entity.User;
import com.doubtshare.security.UserDetailsImpl;
import com.doubtshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
    	System.out.println(registerRequest);
        UserDTO user = userService.register(registerRequest);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/user")
	public ResponseEntity<UserDTO> getUser(@RequestParam String token) {
          User user = userService.getUserByToken(token);
		UserDTO userDto = UserDTO.builder()
				.id(user.getId())
				.email(user.getEmail())
				.userType(user.getUserType())
				.username(user.getUsername())
				.language(user.getLanguage())
				.classGrade(user.getClassGrade())
				.subjectExpertise(user.getSubjectExpertise())
				.teachingGrades(user.getTeachingGrades()).build();
		return ResponseEntity.ok(userDto);
	}
}