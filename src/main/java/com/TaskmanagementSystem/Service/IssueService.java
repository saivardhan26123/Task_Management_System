package com.TaskmanagementSystem.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TaskmanagementSystem.Client.UserClient;
import com.TaskmanagementSystem.DTO.IssueDTO;
import com.TaskmanagementSystem.Entity.Issue;
import com.TaskmanagementSystem.Entity.IssueComent;
import com.TaskmanagementSystem.Enum.IssuePriority;
import com.TaskmanagementSystem.Enum.IssueStatus;
import com.TaskmanagementSystem.Enum.Role;
import com.TaskmanagementSystem.Repository.EpicRepository;
import com.TaskmanagementSystem.Repository.IssueCommentRepository;
import com.TaskmanagementSystem.Repository.IssueRepository;
import com.TaskmanagementSystem.Repository.SprintRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {
	
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private IssueCommentRepository issuecommentRepo;
	
	@Autowired
	private SprintRepository sprintRepo;
	
	@Autowired
	private EpicRepository epicRepo;
	
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private UserClient userClient;
	
	
	private String generatedKey(Long id) {
		return "PROJECT-"+id;
		
		
		
	}
	
	
	@Transactional
	public IssueDTO createIssue(IssueDTO dto) {
		
		Issue issue = new Issue();
		
		issue.setIssueTitle(dto.getIssueTitle());
		issue.setIssueType(dto.getIssueType());
		issue.setIssueKey("PROJECT"+issue.getId());
		issue.setAssigneeEmail(dto.getAssigneeEmail());
		issue.setRepoterEmail(dto.getReporterEmail());
		issue.setCreatedAt(dto.getCreatedAt());
		issue.setDesription(dto.getDesription());
		issue.setPriority(IssuePriority.LOW);
		issue.setStatus(IssueStatus.OPEN);
		issue.setUpdateAt(dto.getUpdateAt());
		issue.setDueDate(dto.getDueDate());
		
		
		if(dto.getEpicId() !=null) {
			epicRepo.findById(dto.getEpicId()).orElseThrow(()-> new RuntimeException("Epic not found"));
			issue.setEpicId(dto.getEpicId());
			
		}
		
		if(dto.getSprintId() !=null) {
			sprintRepo.findById(dto.getSprintId()).orElseThrow(()-> new RuntimeException("Sprint not found"));
			
			issue.setSprintId(dto.getSprintId());
		}
		
		issueRepo.save(issue);
		
		return toDTO(issue);
	}

	
	public IssueDTO getById(Long id) {
		
		Issue issue = issueRepo.findById(id).orElseThrow(()-> new RuntimeException("Issue not found"));
		return toDTO(issue);
	}
	
	
	@Transactional
	public IssueDTO updateIssueStatus(Long id,IssueStatus status,String performBy) {
		
		Issue issue = issueRepo.findById(id).orElseThrow(()-> new RuntimeException("Issue not found"));
		
//		IssueStatus newStatus;
//		
//		try {
//			newStatus = IssueStatus.valueOf(String.valueOf(status).toUpperCase().trim());
//		} catch (Exception e) {
//			throw new RuntimeException("Invalid Status"+status);
//		}
		
		String from = issue.getStatus().name();
		String to =status.name();
		Long workFlowId= issue.getWorkFlowId();
		if(workFlowId==null) {
			throw new RuntimeException("WorkFlow not assigned to issue");
		}
		Set<Role>userRole=userClient.getRole(performBy);
		
		boolean allowed=workFlowService.isTransactionAllowed(workFlowId, from, to, userRole);
		
		if(!allowed) {
			throw new RuntimeException("User"+performBy+"is not allowed to issue from"+from+"->"+to);
		}
		issue.setStatus(status);
		issueRepo.save(issue);
		
		IssueComent comment = new IssueComent();
		comment.setIssueId(id);
		comment.setAuthorEmail(performBy);
		comment.setBody("Status changed from :"+from+"->"+to);
		
		issuecommentRepo.save(comment);
		return toDTO(issue);
		
	}
	
	@Transactional
	public IssueComent addComment(Long issueId,String authorEmail,String body) {
		
		issueRepo.findById(issueId).orElseThrow(()-> new RuntimeException("Issue not found"));
		
		IssueComent comment = new IssueComent();
		comment.setIssueId(issueId);
		comment.setAuthorEmail(authorEmail);
		comment.setBody(body);
		
		return issuecommentRepo.save(comment);
		
	}
	
	
	
	
	public List<IssueDTO>search(Map<String,String>filter){
		
		if(filter.containsKey("assignee")) {
			
			return issueRepo.findByAssigneeEmail(filter.get("assignee")).stream().map(this::toDTO).collect(Collectors.toList());
			
		}
		
		
		if(filter.containsKey("status")) {
			String statusStr= filter.get("status");
			IssueStatus  status;
			
			try {
				status= IssueStatus.valueOf(statusStr.toUpperCase().trim());
				
			} catch (Exception e) {
				throw new RuntimeException("invalid Status:"+statusStr+"| Allowed"+Arrays.toString(IssueStatus.values()));
			}
		
			return issueRepo.findByIssueStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
		
	 }
		
		if(filter.containsKey("sprint")) {
			Long sprintId = Long.valueOf(filter.get("sprint"));
			return issueRepo.findBySprintId(sprintId).stream().map(this::toDTO).collect(Collectors.toList());
		}
		
		return issueRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}


	private IssueDTO toDTO(Issue issue) {
		
		IssueDTO dto = new IssueDTO();
		
		dto.setIssueTitle(issue.getIssueTitle());
		dto.setDesription(issue.getDesription());
		dto.setCreatedAt(issue.getCreatedAt());
		dto.setIssueKey(issue.getIssueKey());
		dto.setIssuePriority(issue.getPriority());
		dto.setIssueStatus(issue.getStatus());
		dto.setIssueType(issue.getIssueType());
		dto.setAssigneeEmail(issue.getAssigneeEmail());
		dto.setReporterEmail(issue.getRepoterEmail());
		dto.setUpdateAt(issue.getUpdateAt());
		dto.setEpicId(issue.getEpicId());
		dto.setSprintId(issue.getSprintId());
		
		
		return dto;
	}
}
