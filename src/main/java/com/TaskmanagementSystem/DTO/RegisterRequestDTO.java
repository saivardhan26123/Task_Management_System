package com.TaskmanagementSystem.DTO;

import com.TaskmanagementSystem.Enum.Role;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

	public String userName;
	public String userOfficialEmail;
	public String password;
	public Role role;
}
