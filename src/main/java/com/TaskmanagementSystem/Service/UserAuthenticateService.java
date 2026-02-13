package com.TaskmanagementSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TaskmanagementSystem.DTO.AuthResponseDTO;
import com.TaskmanagementSystem.DTO.LoginRequestDTO;
import com.TaskmanagementSystem.DTO.RegisterRequestDTO;
import com.TaskmanagementSystem.Entity.UserAuthenticate;
import com.TaskmanagementSystem.Repository.UserAuthenticeRepository;
import com.TaskmanagementSystem.Security.JWTTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthenticateService {
	
	@Autowired
	private UserAuthenticeRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncode;
	
	@Autowired
	private JWTTokenUtil jwtUtil;
	
	public void register(RegisterRequestDTO req) {
		
		if(userRepo.findByUserOfficalEmail(req.userOfficialEmail).isPresent()) {
			throw new RuntimeException("Profile already Exists:" +req.userOfficialEmail);
		}
		UserAuthenticate user =new UserAuthenticate();
		user.setUserName(req.userName);
		user.setUserOfficialEmail(req.userOfficialEmail);
		user.setPassword(passwordEncode.encode(req.password));
		user.setRole(req.role);
		
		userRepo.save(user);
	}
	public AuthResponseDTO login (LoginRequestDTO dto) {
		UserAuthenticate user = userRepo.findByUserOfficalEmail(dto.userOfficialEmail)
				.orElseThrow(()-> new RuntimeException("User not found"));
		
		if(!passwordEncode.matches(dto.password, user.getPassword())) {
		throw new RuntimeException("Invalid Credential");
	}
	
	String token=jwtUtil.genrateToken(user);
	return new AuthResponseDTO(token,"Token got genrate");
}
}
