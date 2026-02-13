# Task Management System (Spring Boot)

A backend-focused **Task Management System** built using **Spring Boot**, inspired by Jira-style workflow tools.  
The application supports boards, sprints, issues, workflows, notifications, authentication, and role-based access control.

This project is designed with a clean layered architecture and focuses on real-world backend practices rather than simple CRUD operations.

---

## Features

### Authentication & Security
- JWT-based authentication
- Role-based access control (RBAC)
- Secure REST APIs using Spring Security
- Token generation and validation

### User Management
- User registration and login
- Role assignment and permission handling
- User profile management

### Board & Project Management
- Create and manage boards
- Board columns and task organization
- Kanban-style workflow support

### Issue Tracking
- Create, update, and track issues
- Issue types, priorities, and statuses
- Epic support
- Comments and attachments

### Sprint & Backlog Management
- Sprint creation and management
- Backlog handling
- Sprint lifecycle tracking

### Workflow Management
- Configurable workflows
- State transitions
- Issue movement tracking

### Notifications
- Event-based notification system
- Notification schemes
- Email notification support

### Reporting
- Basic reporting endpoints for tracking progress and activities

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- Hibernate
- Maven
- MySQL / PostgreSQL
- Cloudinary (for file attachments)

---

## Project Structure
src/main/java/com/TaskmanagementSystem

├── Client

├── Controller

├── DTO

├── Entity

├── Enum

├── Repository

├── Security

├── Service

└── TaskmanagementSystemApplication.java


---

## Setup & Run

### Prerequisites
- Java 17 or higher
- Maven
- MySQL or PostgreSQL

---

### Steps

```bash
git clone https://github.com/your-username/TaskmanagementSystem.git
cd TaskmanagementSystem
mvn clean install
mvn spring-boot:run
```
### Application will run on:
http://localhost:8080

### Configuration

Update application.properties with your database and JWT settings:

spring.datasource.url=jdbc:mysql://localhost:3306/taskmanagement

spring.datasource.username=your_db_username

spring.datasource.password=your_db_password

jwt.secret=your_secret_key

jwt.expiration=86400000

---
### Author

Pippera Sai Vardhan

Backend Developer | Java | Spring Boot | REST APIs

