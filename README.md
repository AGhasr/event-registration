# 🎉 Event Registration System

A Spring Boot web application for managing events with user registration, login, and admin features.

## 📌 Features

- Public list of upcoming events
- User registration with encrypted passwords
- User login with role-based access (User/Admin)
- Admin can create and delete events
- Users can register or cancel event participation
- Prevents duplicate registrations
- Thymeleaf templates with conditional UI (buttons based on role)
- H2 in-memory database for quick setup

---

## 🚀 Technologies Used

- Java 17
- Spring Boot 3
- Spring Security (for authentication & authorization)
- Spring Data JPA (with H2 database)
- Thymeleaf (template engine)
- Bootstrap 5 (basic responsive design)
- Maven

---

## 🎯 How to Run the Application

1. **Clone the repository**

```bash
git clone https://github.com/AGhasr/event-registration.git
cd event-registration-app
```

2. **Build the project**

```bash
mvn clean install
```
3. **Run the application**
```bash
mvn spring-boot:run
```
4. **Access the App**

Open your browser at:
http://localhost:8080

👥 Default Users (In-Memory for Testing)

| Username | Password | Role  |
| -------- |---------| ----- |
| admin    | admin   | ADMIN |


🗂 Project Structure
```bash

src/main/java/org/example/eventregistration
├── controller   # Web controllers (Event, Auth)
├── model        # Entity classes (User, Event)
├── repository   # Spring Data JPA Repositories
├── config       # Security & initial data config
└── EventRegistrationApplication.java

```
Templates are in:
src/main/resources/templates

