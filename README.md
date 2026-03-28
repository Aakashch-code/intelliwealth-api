# 💰 Intelli Wealth

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Spring_Security_6-6db33f?style=flat-square&logo=springsecurity)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=flat-square&logo=jsonwebtokens)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Transactional_Data-blue?style=flat-square&logo=postgresql)
![MongoDB](https://img.shields.io/badge/MongoDB-Financial_Records-47A248?style=flat-square&logo=mongodb)
![Gemini](https://img.shields.io/badge/AI-Google_Gemini-8E75B2?style=flat-square&logo=google)
![React](https://img.shields.io/badge/Frontend-React.js-61DAFB?style=flat-square&logo=react)
![Swagger](https://img.shields.io/badge/Docs-Swagger_UI-85ea2d?style=flat-square&logo=swagger)

**An AI-powered personal finance management platform — built as a solo end-to-end engineering project**

[Live Demo](#-live-demo) · [Frontend Repo](#-frontend-repository) · [Features](#-features) · [Architecture](#-architecture) · [Getting Started](#-getting-started)

</div>

---

## 📌 Overview

**Intelli Wealth** is a backend-first personal finance system designed and built end-to-end as a solo engineering project — demonstrating real-world API design, layered architecture, AI integration, and production-grade security.

The platform goes well beyond CRUD. It models core financial domains — transactions, budgets, goals, assets, debt, insurance, net worth, and contingency planning — with 45+ RESTful endpoints, a unified AI reporting layer powered by Google Gemini, and a thoughtfully structured layered architecture.

---

## 🌐 Live Demo

| Interface | URL |
|-----------|-----|
| 🔗 Backend API (Swagger UI) | [intelliwealth-api.onrender.com/swagger-ui](https://intelliwealth-api.onrender.com/swagger-ui/index.html#/) |
| 🖥️ Frontend Demo | [intelli-wealth-ui.netlify.app](https://intelli-wealth-ui.netlify.app/) |

> ⚠️ Demo runs on a lightweight VM. Expect cold starts and slightly slower AI responses.

---

## 🖥️ Frontend Repository

The React frontend is intentionally minimal — it exists to authenticate users, exercise every secured endpoint, and validate full end-to-end workflows.

> **Frontend Repo:** [github.com/Aakashch-code/intelli-wealth-ui](https://github.com/Aakashch-code/intelli-wealth-ui)

**Stack:** React.js · Axios · JWT auth handling

---

## ✨ Features

### 🔐 Authentication & Security
- Stateless JWT authentication with Spring Security 6
- Role-based access control (RBAC)
- BCrypt password hashing
- Token refresh flow & security filter chain

### 🧠 AI Financial Intelligence (Google Gemini)
- AI-powered financial summaries and personalized insights
- Spending behaviour analysis
- Debt strategy recommendations via a unified reporting system
- Response payloads optimised with **Jackson null-field filtering**

### 💼 Treasury Management
- Budgets, transactions, and subscriptions
- Goal tracking and spending analytics
- 45+ RESTful endpoints covering the full financial lifecycle

### 📊 Contingency Analysis API
- Computes **monthly burn rate**, **liquid assets**, and **financial runway** from aggregated data
- Identifies **recommended savings gap** to reach target emergency coverage
- Driven by a dedicated analytical endpoint, not just raw CRUD

### 💎 Wealth Management
- Asset and liability tracking
- Net worth aggregation
- Category-based classification

### 🛡️ Protection Planning
- Insurance tracking
- Emergency fund planning
- Survival-period estimation and coverage gap detection

---

## 🏗️ Architecture

Intelli Wealth follows a **layered architecture** applied within a **modular monolith** structure — each financial domain owns its full vertical slice.

```
┌────────────────────────────────────────────────────────┐
│                        API Layer                       │
│         REST Controllers · Swagger Docs · DTOs         │
├────────────────────────────────────────────────────────┤
│                   Application Layer                    │
│    Business Logic · Gemini AI Service · Analytics      │
├────────────────────────────────────────────────────────┤
│                     Domain Layer                       │
│      Entities · Domain Models · Business Rules         │
├────────────────────────────────────────────────────────┤
│                  Persistence Layer                     │
│   PostgreSQL (Transactional)  │  MongoDB (Summaries)   │
└────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility |
|-------|---------------|
| **API** | Exposes 45+ REST endpoints; handles request/response mapping, Swagger docs, and input validation |
| **Application** | Orchestrates business workflows, Gemini AI calls, contingency calculations, and cross-domain aggregations |
| **Domain** | Encapsulates core financial rules — net worth computation, runway estimation, coverage gap detection |
| **Persistence** | Polyglot persistence — PostgreSQL for user/transactional data; MongoDB for flexible financial summaries and asset records |

### Package Structure

```
com.example.intelliwealth
├── authentication       # JWT, Spring Security, RBAC
├── treasury             # Budgets, Transactions, Goals, Subscriptions
├── wealth               # Assets, Liabilities, Net Worth
├── protection           # Insurance, Emergency Fund, Contingency
├── advisor              # Gemini AI reporting & unified insights
├── config               # Security config, Beans, Jackson setup
└── exception            # Global error handling
```

---

## 🚀 Getting Started (Backend)

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 15+
- MongoDB 6+
- Google Gemini API Key

### Application Properties

```properties
spring.application.name=Intelli-Wealth
server.port=8085

# PostgreSQL — Transactional & User Data
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=<YOUR_POSTGRES_USERNAME>
spring.datasource.password=<YOUR_POSTGRES_PASSWORD>
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# MongoDB — Financial Summaries & Asset Records
spring.data.mongodb.uri=mongodb://localhost:27017/your_mongo_db
spring.data.mongodb.username=<YOUR_MONGO_USERNAME>
spring.data.mongodb.password=<YOUR_MONGO_PASSWORD>

# Google Gemini AI
gemini.api.key=<YOUR_GEMINI_API_KEY>
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent

# JWT Security
application.security.jwt.secret-key=<YOUR_JWT_SECRET_KEY>
application.security.jwt.expiration=86400000

# Swagger UI
springdoc.swagger-ui.path=/docs
springdoc.swagger-ui.filter=true
```

### Run Locally

```bash
mvn clean install
mvn spring-boot:run
```

Swagger UI available at:
```
http://localhost:8085/swagger-ui/index.html
```

---

## 🧠 What This Project Demonstrates

| Concern | Implementation |
|---------|---------------|
| **API Design** | 45+ RESTful endpoints with clear resource boundaries and consistent response contracts |
| **Layered Architecture** | API → Application → Domain → Persistence with no cross-layer leakage |
| **Security** | Spring Security 6, stateless JWT, RBAC, BCrypt |
| **AI Integration** | Google Gemini for summaries, debt strategies, and behavioural insights |
| **Polyglot Persistence** | PostgreSQL for ACID-critical data; MongoDB for flexible document storage |
| **Analytical APIs** | Contingency engine computing burn rate, runway, and savings gap from aggregated data |
| **Response Optimisation** | Jackson null-field filtering for clean, lightweight AI payloads |

---

## ⚠️ Honest Limitations

- UI is intentionally minimal — the focus is backend correctness
- No automated tests yet (planned)
- No production observability (rate limiting, metrics, tracing)
- AI response quality depends on prompt design and Gemini model version

---

## 📄 License

MIT

---

## 👤 Author

**Aakash Chauhan** — Backend Developer (Java · Spring Boot · System Design)

> Thank you for exploring Intelli Wealth. Feedback and thoughtful discussion on design and implementation choices are always welcome.