# Library System API

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.3-brightgreen.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-Enabled-green.svg)
![Redis](https://img.shields.io/badge/Redis-Caching-red.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)
![Build](https://github.com/ccavalcanti11/library-system-api/actions/workflows/build.yml/badge.svg)

## RESTful API for a Complete Library Management System

Welcome to the **Library System API** — a showcase of clean, modern Java 21 development using Spring Boot. This project demonstrates my ability to build scalable, maintainable, and well-documented RESTful services with a focus on clarity, testing, and best practices.

This comprehensive API manages the complete lifecycle of a library system, from book catalog management to loan tracking and member administration, showcasing enterprise-grade development patterns and modern Java features.

## 📖 Table of Contents

- [🚀 Tech Stack & Architecture](#-tech-stack--architecture)
  - [Core Technologies](#core-technologies)
  - [API & Documentation](#api--documentation)
  - [Testing & Quality](#testing--quality)
  - [Build & Deployment](#build--deployment)
- [📚 API Features & Business Logic](#-api-features--business-logic)
  - [🔖 Book Management](#-book-management)
  - [👤 Author Management](#-author-management)
  - [👥 Borrower Management](#-borrower-management)
  - [📖 Loan Management](#-loan-management-core-business-logic)
  - [🔍 Advanced Search & Filtering](#-advanced-search--filtering)
- [🏗️ Architecture & Design Patterns](#️-architecture--design-patterns)
  - [Clean Architecture Layers](#clean-architecture-layers)
  - [Key Design Decisions](#key-design-decisions)
- [📂 Project Structure](#-project-structure)
- [🛠️ API Endpoints Overview](#️-api-endpoints-overview)
  - [Books API](#books-api-apibooks)
  - [Authors API](#authors-api-apiauthors)
  - [Borrowers API](#borrowers-api-apiborrowers)
  - [Loans API](#loans-api-apiloans)
- [🧪 Testing Strategy](#-testing-strategy)
  - [Unit Tests](#unit-tests)
  - [Integration Tests](#integration-tests)
  - [Test Coverage](#test-coverage)
- [🚀 Performance & Scalability Features](#-performance--scalability-features)
  - [Caching Strategy](#caching-strategy)
  - [Database Optimization](#database-optimization)
  - [API Performance](#api-performance)
- [📖 API Documentation](#-api-documentation)
  - [Interactive Documentation](#interactive-documentation)
  - [OpenAPI Specification](#openapi-specification)
- [🛠️ Quick Start Guide](#️-quick-start-guide)
  - [Prerequisites](#prerequisites)
  - [Docker Compose Setup](#option-1-docker-compose-recommended)
  - [Local Development](#option-2-local-development)
  - [Accessing the Application](#accessing-the-application)
- [🐳 Docker Configuration](#-docker-configuration)
  - [Multi-Service Setup](#multi-service-setup)
  - [Environment Configuration](#environment-configuration)
- [🔍 Monitoring & Observability](#-monitoring--observability)
  - [Health Checks](#health-checks)
  - [Available Endpoints](#available-endpoints)
- [🏆 Best Practices Demonstrated](#-best-practices-demonstrated)
  - [Code Quality](#code-quality)
  - [Security Considerations](#security-considerations)
  - [Production Readiness](#production-readiness)
- [📬 Contact & Collaboration](#-contact--collaboration)

## 🚀 Tech Stack & Architecture

### Core Technologies
- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.3.3** - Enterprise-grade framework
- **Spring Data MongoDB** - NoSQL document database integration
- **Spring Data Redis** - High-performance caching layer
- **Spring Cache** - Declarative caching abstraction
- **Spring Validation** - Comprehensive input validation

### API & Documentation
- **Swagger/OpenAPI 3** - Interactive API documentation
- **Spring Boot Actuator** - Production monitoring and health checks

### Testing & Quality
- **JUnit 5** - Modern testing framework
- **Mockito** - Mocking framework for unit tests
- **Testcontainers** - Integration testing with real databases
- **Spring Boot Test** - Comprehensive test support

### Build & Deployment
- **Gradle** - Modern build automation
- **Docker & Docker Compose** - Containerization and orchestration

## 📚 API Features & Business Logic

### 🔖 Book Management
- **Complete CRUD operations** with validation
- **ISBN-based unique identification** and duplicate prevention
- **Inventory tracking** with available/total copies management
- **Advanced search** by title, genre, author, or keywords
- **Availability checking** for loan eligibility
- **Automated inventory updates** during loan/return operations

### 👤 Author Management
- **Author profiles** with biographical information
- **Email-based unique identification**
- **Genre specialization tracking**
- **Nationality and birth date management**
- **Search by name, nationality, or biography**

### 👥 Borrower Management
- **Member registration** with comprehensive contact information
- **Account activation/deactivation** functionality
- **Membership date tracking**
- **Geographic organization** (city, country filtering)
- **Email-based unique identification**
- **Search and filtering** capabilities

### 📖 Loan Management (Core Business Logic)
- **Intelligent loan creation** with business rule validation
- **Automated availability checking** before loan approval
- **Loan limits enforcement** (max 5 active loans per borrower)
- **Due date management** with configurable loan periods
- **Book return processing** with automatic inventory updates
- **Loan renewal** with overdue prevention
- **Overdue detection** and fine calculation ($0.50/day)
- **Loan status tracking** (ACTIVE, RETURNED, OVERDUE, RENEWED)

### 🔍 Advanced Search & Filtering
- **Multi-field search** across all entities
- **Case-insensitive text matching**
- **Tag-based book discovery**
- **Genre and category filtering**
- **Date range queries** for loans and returns
- **Status-based filtering** for loans and borrowers

## 🏗️ Architecture & Design Patterns

### Clean Architecture Layers
```
┌─────────────────────────────────────────┐
│              Controllers                │  ← REST API Layer
├─────────────────────────────────────────┤
│               Services                  │  ← Business Logic Layer
├─────────────────────────────────────────┤
│             Repositories                │  ← Data Access Layer
├─────────────────────────────────────────┤
│               Models                    │  ← Domain Entities
└─────────────────────────────────────────┘
```

### Key Design Decisions
- **Repository Pattern** for data access abstraction
- **Service Layer** for business logic encapsulation
- **DTO Pattern** implicit in model design
- **Dependency Injection** throughout the application
- **Caching Strategy** for performance optimization
- **Exception Handling** with global error management
- **Validation** at multiple layers (model, service, controller)

## 📂 Project Structure

```
src/
├── main/
│   ├── java/com/librarysystem/
│   │   ├── controller/          # REST endpoints
│   │   │   ├── BookController.java
│   │   │   ├── AuthorController.java
│   │   │   ├── BorrowerController.java
│   │   │   └── LoanController.java
│   │   ├── service/             # Business logic
│   │   │   ├── BookService.java
│   │   │   ├── AuthorService.java
│   │   │   ├── BorrowerService.java
│   │   │   └── LoanService.java
│   │   ├── repository/          # Data access
│   │   │   ├── BookRepository.java
│   │   │   ├── AuthorRepository.java
│   │   │   ├── BorrowerRepository.java
│   │   │   └── LoanRepository.java
│   │   ├── model/               # Domain entities
│   │   │   ├── Book.java
│   │   │   ├── Author.java
│   │   │   ├── Borrower.java
│   │   │   └── Loan.java
│   │   ├── config/              # Configuration
│   │   │   ├── OpenApiConfig.java
│   │   │   └── RedisConfig.java
│   │   └── exception/           # Error handling
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.properties
│       └── application-docker.properties
└── test/
    └── java/com/librarysystem/
        ├── controller/          # Controller tests
        ├── service/             # Unit tests
        └── integration/         # Integration tests
```

## 🛠️ API Endpoints Overview

### Books API (`/api/books`)
- `GET /api/books` - List all books with pagination support
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create new book (with validation)
- `PUT /api/books/{id}` - Update existing book
- `DELETE /api/books/{id}` - Remove book from catalog
- `GET /api/books/isbn/{isbn}` - Find book by ISBN
- `GET /api/books/author/{authorId}` - Books by author
- `GET /api/books/genre/{genre}` - Books by genre
- `GET /api/books/available` - Available books only
- `GET /api/books/search?keyword={keyword}` - Search books
- `GET /api/books/{id}/availability` - Check availability

### Authors API (`/api/authors`)
- `GET /api/authors` - List all authors
- `GET /api/authors/{id}` - Get author by ID
- `POST /api/authors` - Create new author
- `PUT /api/authors/{id}` - Update author information
- `DELETE /api/authors/{id}` - Remove author
- `GET /api/authors/email/{email}` - Find by email
- `GET /api/authors/search?keyword={keyword}` - Search authors
- `GET /api/authors/nationality/{nationality}` - Authors by nationality

### Borrowers API (`/api/borrowers`)
- `GET /api/borrowers` - List all borrowers
- `GET /api/borrowers/{id}` - Get borrower by ID
- `POST /api/borrowers` - Register new borrower
- `PUT /api/borrowers/{id}` - Update borrower information
- `DELETE /api/borrowers/{id}` - Remove borrower
- `GET /api/borrowers/active` - Active members only
- `GET /api/borrowers/search?keyword={keyword}` - Search borrowers
- `PATCH /api/borrowers/{id}/deactivate` - Deactivate account
- `PATCH /api/borrowers/{id}/reactivate` - Reactivate account

### Loans API (`/api/loans`)
- `GET /api/loans` - List all loans
- `GET /api/loans/{id}` - Get loan by ID
- `POST /api/loans` - Create new loan (with business validation)
- `PATCH /api/loans/{id}/return` - Return a book
- `PATCH /api/loans/{id}/renew` - Renew loan period
- `DELETE /api/loans/{id}` - Remove loan record
- `GET /api/loans/borrower/{borrowerId}` - Loans by borrower
- `GET /api/loans/borrower/{borrowerId}/active` - Active loans only
- `GET /api/loans/overdue` - Overdue loans with fines
- `GET /api/loans/due-soon?days={days}` - Loans due soon
- `GET /api/loans/borrower/{borrowerId}/count` - Active loan count

## 🧪 Testing Strategy

### Unit Tests
- **Service Layer Testing** with Mockito for dependencies
- **Controller Testing** with MockMvc for HTTP layer
- **Repository Testing** with embedded MongoDB
- **Business Logic Validation** for loan rules and constraints

### Integration Tests
- **End-to-end API Testing** with Testcontainers
- **Database Integration** with real MongoDB instances
- **Cache Integration** testing with Redis
- **Cross-layer Integration** testing

### Test Coverage
- **High test coverage** across all layers
- **Edge case testing** for business rules
- **Error condition testing** for exception handling
- **Performance testing** for caching effectiveness

## 🚀 Performance & Scalability Features

### Caching Strategy
- **Redis-based caching** for frequently accessed data
- **Declarative caching** with Spring Cache annotations
- **Cache invalidation** on data modifications
- **Configurable TTL** for different data types

### Database Optimization
- **MongoDB indexing** on frequently queried fields
- **Compound indexes** for complex queries
- **Text search indexes** for keyword searches
- **Unique constraints** for data integrity

### API Performance
- **Pagination support** for large result sets
- **Efficient queries** with projection when needed
- **Connection pooling** for database connections
- **Async processing** where applicable

## 📖 API Documentation

### Interactive Documentation
Access the comprehensive Swagger UI documentation at:
**http://localhost:8080/swagger-ui.html**

### OpenAPI Specification
Raw OpenAPI specification available at:
**http://localhost:8080/api-docs**

### Features of API Documentation
- **Interactive endpoint testing** directly from the browser
- **Request/response schema definitions**
- **Authentication requirements** (when applicable)
- **Example requests and responses**
- **Error code documentation**

## 🛠️ Quick Start Guide

### Prerequisites
- **Java 21** or later
- **Docker & Docker Compose** (for containerized setup)
- **MongoDB** (if running locally)
- **Redis** (if running locally)

### Option 1: Docker Compose (Recommended)
```bash
# Clone the repository
git clone https://github.com/yourusername/library-system-api.git
cd library-system-api

# Start all services with Docker Compose
./start.sh
# Or manually:
docker-compose up --build
```

### Option 2: Local Development
```bash
# Start MongoDB and Redis locally, then:
./gradlew bootRun
```

### Accessing the Application
- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **Application Info**: http://localhost:8080/actuator/info

## 🐳 Docker Configuration

### Multi-Service Setup
The application uses Docker Compose to orchestrate:
- **MongoDB** (port 27017) - Primary database
- **Redis** (port 6379) - Caching layer  
- **Library API** (port 8080) - Main application

### Environment Configuration
Production-ready configuration with:
- **Environment-specific properties** files
- **Health checks** for all services
- **Volume persistence** for data
- **Network isolation** between services

## 🔍 Monitoring & Observability

### Health Checks
- **Application health** via Spring Boot Actuator
- **Database connectivity** monitoring
- **Cache system** health checks
- **Custom business metrics** tracking

### Available Endpoints
- `/actuator/health` - Overall application health
- `/actuator/info` - Application information
- `/actuator/metrics` - Performance metrics

## 🏆 Best Practices Demonstrated

### Code Quality
- **Clean Code principles** with meaningful names and small methods
- **SOLID principles** applied throughout the architecture
- **Consistent code formatting** and documentation
- **Comprehensive error handling** with meaningful messages

### Security Considerations
- **Input validation** at multiple layers
- **SQL injection prevention** through parameterized queries
- **Data sanitization** for all user inputs
- **Proper error messages** without sensitive information exposure

### Production Readiness
- **Externalized configuration** for different environments
- **Graceful error handling** and recovery
- **Comprehensive logging** for debugging and monitoring
- **Container orchestration** for scalable deployment

## 📬 Contact & Collaboration

I'm passionate about clean code, modern Java development, and building scalable systems. This project represents my approach to enterprise-grade API development.

**Let's connect:**
- **LinkedIn**: [Carlos Gustavo Cavalcanti](https://www.linkedin.com/in/carlos-gustavo-cavalcanti/)
- **Email**: dev1carloscavalcanti@gmail.com
- **GitHub**: Feel free to explore more of my projects!

---

⭐ **If you found this project valuable, please consider starring the repository!** ⭐

This project showcases modern Java development practices and would be a great reference for:
- **Spring Boot API development**
- **Clean architecture implementation**
- **Comprehensive testing strategies**
- **Docker containerization**
- **MongoDB and Redis integration**
- **Enterprise-grade error handling**
