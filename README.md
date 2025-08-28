# BASS – Build A Sustainable Streak

BASS is a health-focused e-commerce service that promotes nutritious eating by allowing users to easily order balanced meals and keep users motivation up by adding streak based achievement-coupon system. It is designed as a user-facing application built with strong engineering principles and modern backend technologies.

This project is a culmination of our tech course, where we applied what we've learned to a collaborative, production-ready service.

---

## 🚀 Project Objectives

- Build an e-commerce platform using open APIs.
- Encourage healthy eating habits through daily meal ordering.
- Focus on user-facing features while maintaining engineering quality.
- Apply backend best practices including TDD, CI/CD, and infrastructure tools.
- Collaborate effectively as a team building a real, working service.

---

## 🛠️ Tech Stack

| Layer            | Tech/Tool                        |
| ---------------- | -------------------------------- |
| Language         | Kotlin                           |
| Framework        | Spring Boot, Spring Data JPA     |
| Database         | H2 (Dev), PostgreSQL (Prod)      |
| Migration        | Liquibase                        |
| Testing          | JUnit5, MockK                    |
| CI/CD            | GitHub Actions, AWS CodePipeline |
| Deployment       | AWS EC2, AWS RDS                 |
| Containerization | Docker                           |
| Docs             | SpringDoc + Swagger-UI           |
| Build Tool       | Gradle                           |

---

## 🧠 Domain Model Overview

The core domain consists of users, meals, and the ordering process.

### 🗃️ Main Entities

- `MemberEntity`: User data and profile
- `MealEntity`: Meal items available for ordering
- `TagEntity`: Tags describing meal characteristics and preference of user (e.g., vegan, high-protein)
- `DayEntity`: Represents *freedom-day* that user can order non-healthy meal without losing streak
- `AchievementEntity`: Tracks user achievements or health goals
- `CouponEntity`: Discounts or promotions
- `CartItemEntity`: Items in user's cart
- `OrderItemEntity`: Items within an order
- `OrderEntity`: Full user orders

%% > Maybe ER Diagram here %%

---

## 🏗️ Architecture & Project Structure

Although we didn’t strictly follow DDD, the project is organized with clear separation of concerns and an object-oriented design mindset. The structure supports scalability, maintainability, and testability.

```
com.bass
├── advice         # Global exception handling and response advice
├── annotation     # Custom annotations
├── config         # Spring and application-level configurations
├── controller     # REST API controllers
├── dto            # Data Transfer Objects for API requests/responses
├── entities       # JPA entity definitions
├── enums          # Enum classes (status, roles, etc.)
├── events         # Domain or application events
├── exception      # Custom exception classes
├── infrastructure # Infrastructure-related implementations (e.g., adapters, external API clients)
├── mappers        # DTO <-> Entity mapping logic
├── model          # Business models used internally
├── repositories   # Spring Data JPA repository interfaces
├── services       # Business logic and application services
├── util           # Utility/helper classes
└── Application.kt # Application entry point
```

> 🧩 The structure is designed to reflect clean architecture principles, keeping concerns isolated and improving code readability and testing.

---

## 🧪 Testing

We followed **TDD** practices as much as possible. The project includes:

- ✅ Unit tests
- ✅ Integration tests
- ✅ End-to-end tests

### 🔍 Test Coverage Report

| Metric      | Coverage |
|-------------|----------|
| Classes     | 84.7%    |
| Methods     | 78.9%    |
| Branches    | 47.5%    |
| Lines       | 81.7%    |

Run tests with:

```bash
./gradlew test
````

---

## 📦 Installation & Local Setup

### 🐳 Docker (Recommended)

```bash
docker-compose -f ./docker-compose.dev.yml up --build
```

This sets up the backend and database (H2/PostgreSQL depending on config).

### 🛠 Manual Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/SebasNadu/bass.git
    cd bass
    ```
2. Run database locally (optional: via Docker or install PostgreSQL)
3. Configure environment variables:
    - `application.yml` or `.env` for DB credentials, secret keys, etc.
4. Start app:
    ```bash
    ./gradlew bootRun
    ```

---

## 📬 API Documentation

API docs are available via:

> 🔗 `http://localhost:8080/api-docs`

Generated using **SpringDoc + Swagger-UI**.

---

## 🚀 Deployment & CI/CD

We use the following deployment pipeline:

- **CI:** GitHub Actions to run tests, build, and lint code.
- **CD:** AWS CodePipeline to deploy artifacts to AWS EC2 and connect to RDS.

---

## 👥 Team Members

| Name  | GitHub                                                 | Role / Responsibility                                    |
| ----- | ------------------------------------------------------ | -------------------------------------------------------- |
| Bo    | [@bojana-petroska](https://github.com/bojana-petroska) | Backend & Research, Member & Recommendation              |
| Alex  | [@arekiu](https://github.com/arekiu)                   | Backend & FrontEnd, Coupon & Event                       |
| Sebas | [@SebasNadu](https://github.com/SebasNadu)             | Backend & FrontEnd & DevOps, Achievement & Dockerization |
| San   | [@san-ghun](https://github.com/san-ghun)               | Backend & DevOps, Day & Data management                  |

---
## 🔗 Project Resources

| Resource         | Link / Address                                                                        |
| ---------------- | ------------------------------------------------------------------------------------- |
| 🧾 Team Wiki     | [Team Wiki](https://github.com/SebasNadu/bass/wiki)                                   |
| 🎨 Frontend Repo | [Frontend Repository](https://github.com/SebasNadu/bass-frontend)                     |
| 🌐 Live App URL  | [bass.app](https://bass-dev-alb-1133887665.ap-northeast-2.elb.amazonaws.com/api-docs) |

---

## 🪪 License

This project is for educational purposes and is not licensed for commercial use.

---
