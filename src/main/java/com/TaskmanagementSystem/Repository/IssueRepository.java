package com.TaskmanagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.Issue;
import com.TaskmanagementSystem.Enum.IssueStatus;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long>{
	
	Optional<Issue>findByIssueKey(String issueKey);
	Optional<Issue>findById(Long id);
	List<Issue>findByAssigneeEmail(String assigneeEmail);
	List<Issue>findBySprintId(Long sprintId);
	List<Issue>findByIssueStatus(IssueStatus issueStatus);
	List<Issue>findByProjectIdAndSprintIdIsNullOrderByBackLogPosition(Long projectId);
	List<Issue>findByEpicId(Long epicId);
}