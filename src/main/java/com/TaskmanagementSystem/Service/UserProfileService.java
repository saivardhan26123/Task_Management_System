package com.TaskmanagementSystem.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskmanagementSystem.DTO.UserProfileDTO;
import com.TaskmanagementSystem.Entity.UserAuthenticate;
import com.TaskmanagementSystem.Entity.UserProfile;
import com.TaskmanagementSystem.Enum.Role;
import com.TaskmanagementSystem.Repository.UserAuthenticeRepository;
import com.TaskmanagementSystem.Repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private UserAuthenticeRepository userRepo;
	public UserProfileDTO updateUserProfile(UserProfileDTO dto){
		
		UserProfile user = userProfileRepo.findByUserOfficialEmail(dto.userOfficialEamil).orElseThrow(()-> new RuntimeException("User not found"));
		
		UserProfile profile = new UserProfile();
		profile.setUserName(dto.userName);
		profile.setUserOfficialEmail(dto.userOfficialEamil);
		profile.setDepartment(dto.department);
		profile.setDesignation(dto.designation);
		profile.setOrganizationName(dto.organizationName);
		profile.setCreatedAt(LocalDateTime.now());
		profile.setActive(true);
		
		userProfileRepo.save(profile);
		
		return toDTO(profile);
		
	}
	
	public UserAuthenticate uploadRole(Long id,Role role) {
		UserAuthenticate user = userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
		
		user.setRole(role);
		return userRepo.save(user);
	}
	public List<UserProfileDTO>getAllUserProfile(){
		return userProfileRepo.findAll().stream().map(this:: toDTO).collect(Collectors.toList());
	}
	
	public UserProfileDTO getProfileByEmail(String userOfficialEmail) {
		UserProfile user = userProfileRepo.findByUserOfficialEmail(userOfficialEmail).orElseThrow(()-> new RuntimeException("User not found"));
		return toDTO(user);
	}
	
	public List<UserProfileDTO>getProfileByDepartment(String department){
		return userProfileRepo.findUserByDepartment(department).stream().map(this:: toDTO).collect(Collectors.toList());
	}
	
	public List<UserProfileDTO>getProfileByDesignation(String designation){
		return userProfileRepo.findUserByDesignation(designation).stream().map(this:: toDTO).collect(Collectors.toList());
	}
	private UserProfileDTO toDTO(UserProfile user) {
		UserProfileDTO dto= new UserProfileDTO();
		dto.setUserName(user.getUserName());
		dto.setUserOfficialEamil(user.getUserOfficialEmail());
		dto.setDepartment(user.getDepartment());
		dto.setDesignation(user.getDesignation());
		dto.setOrganizationName(user.getOrganizationName());
		dto.setActive(user.isActive());
		dto.setCreatedAt(LocalDateTime.now());
		
		return dto;
	}

}
