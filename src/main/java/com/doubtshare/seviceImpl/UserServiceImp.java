package com.doubtshare.seviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.doubtshare.dto.LoginRequest;
import com.doubtshare.dto.LoginResponse;
import com.doubtshare.dto.RegisterRequest;
import com.doubtshare.dto.UserDTO;
import com.doubtshare.entity.User;
import com.doubtshare.repository.UserRepository;
import com.doubtshare.security.JwtUtil;
import com.doubtshare.security.UserDetailsImpl;
import com.doubtshare.service.UserService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
	private final UserRepository userRepository;
	private final JwtUtil jwtUitil;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final AuthenticationManager  authentication;
	

	@Override
	public UserDTO register(RegisterRequest request) {
		
			User user = User.builder()
						.email(request.getEmail())
						.password(passwordEncoder.encode(request.getPassword()))
						.username(request.getUsername())
						.fullName(request.getFullName())
						.language(request.getLanguage())
						.classGrade(request.getClassGrade())
						.userType(request.getUserType())
						.subjectExpertise(request.getSubjectExpertise())
						.teachingGrades(request.getTeachingGrades())
						.build();
			User saveUser = userRepository.save(user);
			
			return dataMapper(saveUser);
			
					
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		
		 authentication.authenticate(
	                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = userRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new RuntimeException("User not found with username: " + request.getUsername()));
	        return new LoginResponse(jwtUitil.generateToken(new UserDetailsImpl(user)), null);
	}

	@Override
	public User getUserByToken(String actualToken) {
		String username = jwtUitil.extractUsername(actualToken); // Extract username from token
	    return userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));	}
	
	public UserDTO dataMapper(User user) {
		UserDTO usr = UserDTO.builder()
						.email(user.getEmail())
						.username(user.getUsername())
						.language(user.getLanguage())
						.classGrade(user.getClassGrade())
						.userType(user.getUserType())
						.subjectExpertise(user.getSubjectExpertise())
						.teachingGrades(user.getTeachingGrades())
						.build();
		return usr;
	}
	

}
