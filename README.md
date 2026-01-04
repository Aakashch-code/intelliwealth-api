# 💰 Intelli Wealth - Backend API

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?style=flat-square&logo=postgresql)
![AI](https://img.shields.io/badge/AI_Engine-Fynix-purple?style=flat-square&logo=openai)
![Swagger](https://img.shields.io/badge/Docs-Swagger_UI-85ea2d?style=flat-square&logo=swagger)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

**Intelli Wealth** is a modular personal finance engine built with Spring Boot. It provides a comprehensive set of RESTful APIs to track wealth, manage liabilities, calculate financial health metrics, and generate AI-driven insights via the custom **Fynix** engine.

This repository hosts the **backend core**, focusing on domain-driven design and a clean "Package-by-Feature" architecture.

> **Current Status:** 🚧 Active Development. (Security layers and Frontend integration are planned for future updates).

---

## 🔌 API Documentation & Architecture

The application exposes a fully documented REST API via **Swagger UI**.

### API Modules
The backend is divided into distinct functional domains as seen in the API interface:

* **Fynix AI:** Intelligent financial chat and summary generation.
* **Wealth Management:** Net worth aggregation, Asset tracking, and Debt management.
* **Protection:** Insurance policy tracking and Contingency (Emergency Fund) planning.
* **Core Finance:** Budgeting, Goals, Transactions, and Subscriptions.

![img.png](img.png)
---

## 🌟 Key Features

### 🧠 Fynix AI Module
* **Financial Chatbot:** `FinancialChatController` handles context-aware queries about user data.
* **Smart Summaries:** Automated extraction of key financial metrics and recommendations.
* **Prompt Engineering:** Custom `FynixPromptBuilder` and `AiJsonExtractor` for structured AI responses.

### 🛡️ Protection & Contingency
* **Insurance Engine:** Manages Life, Health, and General insurance with specific attributes (frequency, premium, coverage).
* **Contingency Planning:** Specialized logic to calculate financial survival capability in months based on liquid assets.

### 📈 Wealth Management
* **Asset & Debt Tracking:** Dedicated controllers for managing Assets (`Real Estate`, `Gold`, `Mutual Funds`) and Liabilities.
* **Net Worth Engine:** Real-time calculation service that aggregates data from Asset and Debt repositories.
* **Attribute Rules:** Dynamic validation rules for different asset/debt categories.

### 💸 Core Services
* **Smart Budgeting:** Compare actual spending against defined budgets.
* **Goal Tracking:** Monitor progress toward specific financial targets (e.g., "Buy a House").
* **Subscription Manager:** Tracks recurring payments to identify fixed monthly costs.

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **Database** | PostgreSQL |
| **AI Integration** | Custom AI Service / Ollama (Local) |
| **Documentation** | Swagger / OpenAPI 3.0 |
| **Build Tool** | Maven |
| **Architecture** | Modular Monolith (Package-by-Feature) |

---

## 📂 Project Structure

The project adopts a Package-by-Feature (vertical slicing) architecture,
where each feature encapsulates its controllers, services, repositories, DTOs, and 
domain logic. This design ensures high cohesion, clear boundaries, and scalable growth as 
the application evolves.
```text
src
└── main
    └── java
        └── com.example.intelliwealth
            │
            ├── config
            │   ├── AIConfig.java
            │   ├── CurrencySerializer.java
            │   ├── SwaggerConfig.java
            │   └── WebConfig.java
            │
            ├── exception
            │   ├── ApiError.java
            │   ├── GlobalExceptionHandler.java
            │   └── GoalNotFoundException.java
            │
            ├── core
            │   ├── budget
            │   │   ├── Budget.java
            │   │   ├── BudgetController.java
            │   │   ├── BudgetService.java
            │   │   ├── BudgetRepository.java
            │   │   ├── BudgetMapper.java
            │   │   ├── BudgetRequestDTO.java
            │   │   ├── BudgetResponseDTO.java
            │   │   └── BudgetSummaryDTO.java
            │   │
            │   ├── goal
            │   │   ├── Goal.java
            │   │   ├── GoalController.java
            │   │   ├── GoalService.java
            │   │   ├── GoalRepository.java
            │   │   ├── GoalRequestDTO.java
            │   │   ├── AddFundsRequestDTO.java
            │   │   ├── GoalResponseDTO.java
            │   │   └── GoalStatsResponseDTO.java
            │   │
            │   ├── subscription
            │   │   ├── Subscription.java
            │   │   ├── SubscriptionController.java
            │   │   ├── SubscriptionService.java
            │   │   ├── SubscriptionRepository.java
            │   │   ├── SubscriptionMapper.java
            │   │   ├── SubscriptionRequestDTO.java
            │   │   └── SubscriptionResponseDTO.java
            │   │
            │   └── transaction
            │       ├── Transaction.java
            │       ├── TransactionController.java
            │       ├── TransactionService.java
            │       ├── TransactionsRepository.java
            │       ├── TransactionMapper.java
            │       ├── TransactionRequestDTO.java
            │       └── TransactionResponseDTO.java
            │
            ├── fynix
            │   ├── controller
            │   ├── dto
            │   ├── model
            │   ├── repository
            │   ├── service
            │   └── util
            │
            ├── protection
            │   ├── contingency
            │   │   ├── ContingencyController.java
            │   │   ├── ContingencyService.java
            │   │   └── ContingencyReportDTO.java
            │   │
            │   └── insurance
            │       ├── controller
            │       ├── service
            │       ├── repository
            │       ├── dto
            │       ├── mapper
            │       ├── domain
            │       └── validation
            │
            └── wealth
                ├── asset
                │   ├── controller
                │   ├── service
                │   ├── repository
                │   ├── dto
                │   ├── mapper
                │   ├── domain
                │   ├── validation
                │   └── exception
                │
                ├── debt
                │   ├── controller
                │   ├── service
                │   ├── repository
                │   ├── dto
                │   ├── mapper
                │   ├── domain
                │   ├── validation
                │   └── exception
                │
                └── networth
                    ├── NetWorthController.java
                    ├── NetWorthService.java
                    └── NetWorthResponseDTO.java
