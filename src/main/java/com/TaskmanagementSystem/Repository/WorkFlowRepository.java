package com.TaskmanagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.WorkFlow;

@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long>{
	
	Optional<WorkFlow>findByTransactionNmae(String transactionName);
	Optional<WorkFlow>findByWorkFlowName(String workFlowName);

}
