package com.TaskmanagementSystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TaskmanagementSystem.Service.EmailService;

@RestController
@RequestMapping("/api/notifications")
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@PostMapping("/send")
	public ResponseEntity<String>sendEmail(@PathVariable String recipientEmail,@PathVariable  String subject,@PathVariable  String body){
		emailService.send(recipientEmail, subject, body);
		return ResponseEntity.ok("Email sent sucessfully");
	}
}
