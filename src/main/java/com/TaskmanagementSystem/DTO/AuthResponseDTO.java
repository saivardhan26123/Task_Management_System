package com.TaskmanagementSystem.DTO;

import lombok.*;

@Data
@Builder
public class AuthResponseDTO {

	public String token;
	private String message;
	
	public AuthResponseDTO() {}
	public AuthResponseDTO(String token, String message) {
		this.token=token;
		this.message=message;
	}
}
