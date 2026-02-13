package com. TaskmanagementSystem.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation. PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com. TaskmanagementSystem.Enum. IssueStatus;
@FeignClient(name="issue-service", url="${issue-service.url}")
public interface IssueClient {
	@PutMapping("/api/issues/{id}/status")
	void updateStatus (@PathVariable Long id,
			@RequestParam IssueStatus status,
			@RequestParam String performedBy);
	@PostMapping("/api/issues/{id}/comment")
	void addComment(@PathVariable Long id,
			@RequestParam String author, 
			@RequestParam String body);
}