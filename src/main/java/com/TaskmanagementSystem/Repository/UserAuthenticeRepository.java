package com.TaskmanagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.UserAuthenticate;

@Repository
public interface UserAuthenticeRepository extends JpaRepository<UserAuthenticate,Long>{
	Optional<UserAuthenticate>findByUserOfficalEmail(String userOfficalEmail);

}
