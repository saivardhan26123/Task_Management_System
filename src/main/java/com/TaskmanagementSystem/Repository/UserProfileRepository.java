package com.TaskmanagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long>{

	Optional<UserProfile>findByUserOfficialEmail(String userOfficalEmail);
	List<UserProfile>findUserByDesignation(String designation);
	List<UserProfile>findUserByDepartment(String department);
	
	
}
