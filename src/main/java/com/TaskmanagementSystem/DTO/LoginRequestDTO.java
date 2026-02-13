package com.TaskmanagementSystem.DTO;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

	public String userOfficialEmail;
	public String password;
}
