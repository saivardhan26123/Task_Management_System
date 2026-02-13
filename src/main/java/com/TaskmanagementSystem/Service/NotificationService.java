package com.TaskmanagementSystem.Service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskmanagementSystem.Entity.Notification;
import com.TaskmanagementSystem.Entity.NotificationScheme;
import com.TaskmanagementSystem.Enum.NotificationEvent;
import com.TaskmanagementSystem.Repository.NotificationRepository;
import com.TaskmanagementSystem.Repository.NotificationSchemeRepository;

@Service
public class NotificationService {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private NotificationRepository notificationRepo;
	
	
	@Autowired
	private NotificationSchemeRepository notificationSchemeRepo;
	
	
	
	public void notify(Long projecId,
			           NotificationEvent eventType,
			           Set<String> emails,
			           String subject,
			           String message,
			           Long entityId) {
		
		Set<String>finalReciients=notificationSchemeRepo.findByProjectAndEventType(projecId, eventType).
				                            map(scheme->resolveRecepient(scheme,emails)).orElse(emails);
		
		for(String email:finalReciients) {
			
			emailService.send(email, subject, message);
			
			Notification notification= new Notification();
			notification.setRecipientEmail(email);
			notification.setSubject(subject);
			notification.setBody(message);
			notification.setEvent(eventType);
			notification.setEntityId(entityId);
			
			notificationRepo.save(notification);
		}
	}
	
	private Set<String>resolveRecepient(NotificationScheme scheme,Set<String> fallBack){
		return scheme.getReceipient();
	}
}