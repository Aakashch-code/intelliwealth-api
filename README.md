# 💰 Intelli Wealth - Backend API

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?style=flat-square&logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

**Intelli Wealth** is the backend server for a comprehensive personal finance management platform. It exposes a robust RESTful API designed to track wealth, manage diverse assets, and calculate real-time financial health metrics.

This repository contains the **server-side logic**, database architecture, and security implementation (JWT) that powers the user dashboard.

---

## 📸 API Output & Frontend Integration
*Note: This repository hosts the backend code. The images below demonstrate how the API data is consumed and rendered by the client application.*

### 1. Dashboard - Populated Data
> **Backend Role:** The API aggregates Net Worth (Assets - Liabilities), fetches the last 10 transactional records, calculates monthly spending vs. budget, and retrieves active goals via the `/api/v1/dashboard/overview` endpoint.

![Dashboard Populated](https://github.com/user-attachments/assets/2eafa37b-63b0-4054-9ca7-a8037821243a)

### 2. Dashboard - Empty State
> **Backend Role:** The API handles new user states gracefully, returning standardized zero-values and empty DTO lists to ensure the frontend renders a clean "Welcome" state without crashing.

![Dashboard Empty](https://github.com/user-attachments/assets/ed82ffef-e673-40fd-af53-716ee7a143e7)

---

## 🌟 Key Features

### 🔐 Security & Auth
* **JWT Authentication:** Stateless session management using JSON Web Tokens.
* **Role-Based Access:** Secure endpoints for user data protection.

### 💸 Core Finance Engine
* **Transaction Management:** CRUD operations for incomes and expenses with categorization.
* **Budgeting System:** Logic to compare defined budgets against actual transaction data.
* **Subscription Tracker:** Monitors recurring expenses (e.g., Netflix, Gym) to calculate monthly fixed costs.

### 📈 Wealth & Analytics
* **Net Worth Calculator:** Real-time aggregation formula: `(Total Assets + Liquid Cash) - (Liabilities)`.
* **Asset Portfolio:** Supports multiple asset classes:
    * *Real Estate* (Commercial/Residential)
    * *Market Instruments* (Bonds, Gold, Mutual Funds)
* **Goal Tracking:** Progress calculation logic for specific savings targets.

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **Database** | PostgreSQL |
| **Security** | Spring Security & JWT |
| **Build Tool** | Maven |
| **Architecture** | Layered (Controller, Service, Repository) |

---

## 📂 Project Structure

A look at the layered architecture used to separate concerns:

```text
src/main/java/com/intelliwealth
├── config       # Security configuration & CORS setup
├── controller   # REST API Endpoints (Request/Response handling)
├── service      # Business Logic (Calculations, Validations)
├── repository   # JPA Interfaces for Database Access
├── entity       # Database Models (Hibernate Entities)
├── dto          # Data Transfer Objects (API Payloads)
└── utils        # JWT Utilities & Helper classes
