# 💰 Intelli Wealth (Personal Project)

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square\&logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Spring_Security-6db33f?style=flat-square\&logo=springsecurity)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=flat-square\&logo=jsonwebtokens)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?style=flat-square\&logo=postgresql)
![MongoDB](https://img.shields.io/badge/NoSQL-MongoDB-47A248?style=flat-square\&logo=mongodb)
![AI](https://img.shields.io/badge/AI_Engine-Fynix-purple?style=flat-square\&logo=openai)
![Swagger](https://img.shields.io/badge/Docs-Swagger_UI-85ea2d?style=flat-square\&logo=swagger)

**An AI-powered personal finance management platform — built as a solo engineering project**

[Live Demo](#-live-demo) • [Frontend Repo](#-frontend-repository) • [Features](#-features) • [Architecture](#-architecture) • [Getting Started](#-getting-started)

</div>

---

## 📌 Overview

**Intelli Wealth** is a backend-first personal finance system that I designed and built end-to-end as a personal engineering project to practice and demonstrate real-world backend architecture, security, and AI integration.

The project goes beyond basic CRUD functionality. It models core financial domains such as treasury, wealth, protection, and advisory with clear boundaries and maintainable design, while emphasizing production-grade authentication, authorization, and an extensible AI layer for future intelligence-driven features.
### Why this project exists

* To prove backend depth beyond basic REST CRUD
* To demonstrate modular monolith design
* To integrate local LLMs (Ollama) into a real business workflow
* To build something resume‑credible

---

## 🌐 Live Demo

> **Backend API (Swagger UI)**
> https://intelliwealth-api.onrender.com/swagger-ui/index.html#/

> **Frontend Demo**
> https://intelli-wealth-ui.netlify.app/

⚠️ Demo runs on a lightweight VM with limited resources. Expect cold starts and slow AI responses.

---

## 🖥️ Frontend Repository

The frontend is intentionally kept lightweight and API‑driven. It exists to:

* Authenticate users
* Call every secured backend endpoint
* Validate full end‑to‑end flows

> **Frontend Repo**
https://github.com/Aakashch-code/intelli-wealth-ui

**Tech used:**

* React.js
* Axios
* JWT auth handling
* Minimal UI (focus is backend correctness, not UI polish)

---

## ✨ Features

### 🔐 Authentication & Security

* Stateless JWT authentication
* Role‑based access control (RBAC)
* BCrypt password hashing
* Token refresh flow
* Spring Security filter chain

### 🧠 Fynix AI Engine

* Context‑aware financial assistant
* AI‑generated financial summaries
* Spending behavior analysis
* Rule‑guided deterministic prompts
* Local LLM support via Ollama

### 💼 Treasury Management

* Budgets
* Transactions
* Subscriptions
* Goals
* Spending analytics

### 💎 Wealth Management

* Assets
* Liabilities
* Net worth aggregation
* Category‑based classification

### 🛡️ Protection Planning

* Insurance tracking
* Emergency fund planning
* Survival‑period estimation
* Coverage gap detection

---

## 🏗️ Architecture

**Modular Monolith** with package‑by‑feature organization.

* Each domain owns:

    * Controller
    * Service
    * Repository
    * DTOs
    * Domain models

* Clear boundaries

* No god‑services

* Vertical slicing

```
com.example.intelliwealth
├── authentication
├── fynix
├── treasury
├── wealth
├── protection
├── advisor
├── config
└── exception
```

---

## 🚀 Getting Started (Backend)

### Prerequisites

* Java 21+
* Maven 3.8+
* PostgreSQL 15+
* MongoDB 6+ (for AI features)
* Ollama (optional, for local LLM)

---

### Application Properties

```properties
spring.application.name=Intelli-Wealth
server.port=8085

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=<YOUR_POSTGRES_USERNAME>
spring.datasource.password=<YOUR_POSTGRES_PASSWORD>
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Swagger UI
springdoc.swagger-ui.path=/docs
springdoc.swagger-ui.filter=true

# AI / Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2
spring.ai.ollama.chat.options.temperature=0.7

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/your_mongo_db
spring.data.mongodb.username=<YOUR_MONGO_USERNAME>
spring.data.mongodb.password=<YOUR_MONGO_PASSWORD>

# JWT Security
application.security.jwt.secret-key=<YOUR_JWT_SECRET_KEY>
application.security.jwt.expiration=86400000
```

---

### Run Locally

```bash
mvn clean install
mvn spring-boot:run
```

Swagger:

```
http://localhost:8085/swagger-ui/index.html#/
```

---

## 🧠 What This Project Demonstrates

* Real domain modeling (not fake entities)
* Secure JWT auth with RBAC
* Modular backend architecture
* AI integration into business logic
* Polyglot persistence (Postgres + MongoDB)
* Resume‑grade backend complexity

---

## ⚠️ Honest Limitations

* UI is basic by design
* No automated tests yet
* No production hardening (rate limits, observability)
* AI accuracy depends on prompt + model quality

---

## 📄 License

MIT

---

## 👤 Author

**Aakash Chauhan**
Backend Developer (Java, Spring Boot)

---
* Thank you for taking the time to explore Intelli Wealth. I welcome feedback and thoughtful discussion around the design and implementation choices in this project.
---