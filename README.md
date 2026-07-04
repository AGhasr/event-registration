# Gather

Gather is a Spring Boot application for group event and trip planning. Its core feature is a shared expense and debt calculation system (similar to Splitwise) integrated directly into the planning process, allowing groups to coordinate events and settle balances in one place.

**Live Demo:** [https://gather-xrz0.onrender.com](https://gather-xrz0.onrender.com) 
*(Note: This is hosted on Render's free tier, so the server may take a minute or two to wake up if inactive.)*

---

## Features

* **Event Management:** Create events, manage registrations, and view group schedules on a central dashboard.
  ![Events Page](screenshots/events.png)
* **Expense Tracking:** Log shared costs for a group. The app automatically calculates per-user balances and splits costs evenly.
  ![Expenses Wallet](screenshots/expenses.png)
* **Debt Settlement:** View a global breakdown of who owes what across different groups and track manual settlements.
  ![Debts Page](screenshots/debts.png)
* **Group Chat & Polls:** Real-time WebSockets-based messaging and polling for coordination.
  ![Chat with Poll](screenshots/chat.png)
* **Access Control:** User registration with email verification, JWT authentication, and protected group invite links.

---

## Tech Stack & Architecture

* **Backend:** Java 17, Spring Boot (MVC, REST, Security, Data JPA), Hibernate
* **Database:** H2 (default for development)
* **Frontend:** Thymeleaf
* **Real-time:** WebSockets (STOMP)
* **Auth:** JWT

The application is modularized into several core domains: `auth`, `groups`, `events`, `chat`, `expenses`, and `debts`. Each module handles its own MVC controllers, REST API endpoints, business logic, and database interactions.

---

## Example API Endpoints

Most endpoints (except authentication) require a valid JWT token.

### Authentication
* `POST /api/auth/register`
* `POST /api/auth/login`
* `POST /api/auth/verify`

### Groups & Events
* `GET /api/groups`
* `POST /api/groups`
* `POST /api/events/new?groupId=1`
* `POST /api/events/register/{eventId}`

### Expenses
* `GET /api/groups/{groupId}/expenses`
* `POST /api/groups/{groupId}/expenses`

---

## Running Locally

**Prerequisites:**
* Java 17+
* Maven

Clone the repository and run the following command:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

**Demo Data:**
By default, the application seeds test data on startup, including test accounts (e.g., username `ali` / password `1234` or `tom` / `1234`). You can disable this behavior for production by updating your configuration:

```properties
app.db.seed=false
```

## Roadmap

* Profile picture uploads
* Location and map pin sharing in group chat
* Web push notifications for new messages
* Support for recurring events
* Advanced expense splitting (percentage-based or exact amounts, rather than just even splits)
