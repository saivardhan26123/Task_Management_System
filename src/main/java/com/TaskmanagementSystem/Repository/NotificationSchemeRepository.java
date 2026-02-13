package com.TaskmanagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.NotificationScheme;
import com.TaskmanagementSystem.Enum.NotificationEvent;

@Repository
public interface NotificationSchemeRepository extends JpaRepository<NotificationScheme,Long>{

	Optional<NotificationScheme>findByProjectAndEventType(Long projectId,NotificationEvent eventType);
}
