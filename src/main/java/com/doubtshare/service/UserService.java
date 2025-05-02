package com.doubtshare.service;

import com.doubtshare.dto.LoginRequest;
import com.doubtshare.dto.LoginResponse;
import com.doubtshare.dto.RegisterRequest;
import com.doubtshare.dto.UserDTO;
import com.doubtshare.entity.User;

public interface UserService {
	public UserDTO register(RegisterRequest request);
	public LoginResponse login(LoginRequest request);
	public User getUserByToken(String actualToken);
}
