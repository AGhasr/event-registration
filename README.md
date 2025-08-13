# 🎉 Event Registration System

A Spring Boot application for managing events with **both web (Thymeleaf)** and **REST API** access, now secured using **JWT authentication**.

## 📌 Features

- Public list of upcoming events
- User registration with encrypted passwords
- **JWT-based authentication** for REST API access
- **REST API endpoints** for authentication, event registration, and listing events
- User login with role-based access (User/Admin)
- Admin can create and delete events
- Users can register or cancel event participation
- Prevents duplicate registrations
- Thymeleaf templates for the web interface (role-based UI rendering)
- H2 in-memory database for quick setup

---

## 🚀 Technologies Used

- Java 17
- Spring Boot 3
- Spring Security (JWT authentication & role-based access control)
- Spring Data JPA (H2 database)
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

### 🔑 Setting the JWT Secret

Before running the app, you must set the environment variable `JWT_SECRET`:

- Linux/macOS:
```bash
export JWT_SECRET="your-very-long-base64-secret=="
```
3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the web app**
```
http://localhost:8080
```

5. **Test the REST API (JWT required)**  
   Example: Register
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "ali",
  "password": "1234"
}
```

Example: Login (get JWT token)
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "ali",
  "password": "1234"
}
```
Response will contain the `token` — use it in the Authorization header for API requests:
```
Authorization: Bearer <token>
```

---

## 👥 Default Web Users

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin    | ADMIN |

---

## 🗂 Project Structure
```
src/main/java/org/example/eventregistration
├── config       # Security config, JWT filter, data loader
├── controller   # Web controllers & REST API controllers
├── dto          # Data Transfer Objects (EventDTO, Auth DTOs)
├── model        # Entity classes (User, Event)
├── repository   # Spring Data JPA repositories
├── security     # JWT-related classes (JwtService, JwtAuthFilter)
└── service      # Business logic (CustomUserDetailsService)
```

Templates for the web UI are in:
```
src/main/resources/templates
```
