package com.TaskmanagementSystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TaskmanagementSystem.DTO.AuthResponseDTO;
import com.TaskmanagementSystem.DTO.LoginRequestDTO;
import com.TaskmanagementSystem.DTO.RegisterRequestDTO;
import com.TaskmanagementSystem.Service.UserAuthenticateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthenticateController {
	@Autowired
	private UserAuthenticateService authService;

	@PostMapping("/register")
	public ResponseEntity<String>register(@RequestBody RegisterRequestDTO dto){
		authService.register(dto);
		return ResponseEntity.ok("User register Successful");
	}
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO>login(@RequestBody LoginRequestDTO dto){
		return ResponseEntity.ok(authService.login(dto));
	}
}
