package com.TaskmanagementSystem.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskmanagementSystem.Entity.WorkFlow;
import com.TaskmanagementSystem.Entity.WorkFlowTransaction;
import com.TaskmanagementSystem.Enum.Role;
import com.TaskmanagementSystem.Repository.WorkFlowRepository;
import com.TaskmanagementSystem.Repository.WorkFlowTransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class WorkFlowService {
	 @Autowired
	 private WorkFlowRepository workflowRepo;
	 
	 @Autowired
	 private WorkFlowTransactionRepository workflowTransRepo;
	 
	 @Transactional
	 public WorkFlow createWorkFlow(WorkFlow wf) {
		 for(WorkFlowTransaction trans:wf.getTransaction())trans.setWorkflow(wf);
		 return workflowRepo.save(wf);

	 }
	 public List<WorkFlow>listAll(){
		 return workflowRepo.findAll();
	 }
	 public WorkFlow getById(Long id) {
		 return workflowRepo.getById(id);
	 }
	 public Optional<WorkFlow>findByWorkFlowName(String workFlowName){
		 return workflowRepo.findByWorkFlowName(workFlowName);
	 }
	 
	 @Transactional
	 public WorkFlow UpdateWorkFlowStatus(Long id,WorkFlow updated) {
		 
		 WorkFlow wf = getById(id);
		 wf.setWorkFlowName(updated.getWorkFlowName());
		 wf.setWorkFlowDescription(updated.getWorkFlowDescription());
		 wf.getTransaction().clear();
		 
		 if(updated.getTransaction()!=null) {
			 for(WorkFlowTransaction trasn:updated.getTransaction()) {
				 trasn.setWorkflow(wf);
				 wf.getTransaction().add(trasn);
			 }
		 }
			 
		 return workflowRepo.save(wf);
		 
	 }
	 public void deleteWorkFlow(Long id) {
		 workflowRepo.deleteById(id);
	 }
	 public List<WorkFlowTransaction>allowedTransactions(Long workFlowId,String fromStatus){
		 return workflowTransRepo.findByWorkFlowIdFromStatus(workFlowId, fromStatus);
	 }
	 public boolean isTransactionAllowed(Long workFlowId,String fromStatus,String toStatus,Set<Role>userRole){
		 
		 List<WorkFlowTransaction>list=workflowTransRepo.findByWorkFlowIdFromStatus(workFlowId, fromStatus);
		 for(WorkFlowTransaction trans :list) {
			 if(trans.getToStatus().equals(toStatus)) {
				 String allowed=trans.getAllowedRole();
				 if(allowed == null || allowed.isEmpty()) return true;
				 
				 Set<String> allowedSet =Arrays.stream(allowed.split(",")).map(String::trim).collect(Collectors.toSet());
				 for(Role r :userRole) if(allowedSet.contains(r)) return true;
				 return false;
			 }
		 }
		 return false;
	 }
	 

}
