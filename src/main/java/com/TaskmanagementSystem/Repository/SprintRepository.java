package com.TaskmanagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.Sprint;
import com.TaskmanagementSystem.Enum.SprintState;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long>{

	List<Sprint>findByProjectId(Long projectId);
	List<Sprint>findByState(SprintState state);
}