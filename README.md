# RxDesk — Helpdesk Ticketing System

🚀 A full-stack **Java Spring Boot** ticketing system with a simple **HTML/JavaScript UI**, deployed live on Render.

👉 **[Live Demo](https://rxdesk-api.onrender.com/)**

---

## Features
- 🎫 Ticket lifecycle: **Create → Start Work → Assign → Close**
- 🏷️ Priority system (LOW / MEDIUM / HIGH) with automatic sorting
- 🔎 Filtering by keyword, status, priority, or assigned user
- 📊 Statistics endpoint with counts + average resolution time
- 🌐 REST API backend (`/api/tickets`) + frontend UI (`index.html`)
- ☁️ Deployed via **Docker** on **Render**, with CI/CD from GitHub

---

## Tech Stack
- **Backend**: Java 17, Spring Boot 3
- **Frontend**: HTML, CSS, JavaScript (Fetch API)
- **DevOps**: Docker, Render (cloud hosting)
- **Tools**: Git, GitHub, IntelliJ IDEA

---

## API Endpoints
- `GET /api/tickets` → list tickets
- `POST /api/tickets` → create new ticket
- `POST /api/tickets/{id}/start` → set status to `IN_PROGRESS`
- `POST /api/tickets/{id}/assign` → assign user `{ "assignee": "Alice" }`
- `POST /api/tickets/{id}/close` → close a ticket
- `GET /api/tickets/stats` → status & priority breakdown + avg resolution time

---

## Screenshots
*(Add some screenshots here after you take them from your live site — UI page, ticket table, stats panel, etc.)*

---

## How to Run Locally
1. Clone the repo:
   ```bash
   git clone https://github.com/Zulfi-17/rxdesk-api.git
   cd rxdesk-api
