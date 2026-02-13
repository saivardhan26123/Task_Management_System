package com.TaskmanagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{

}
