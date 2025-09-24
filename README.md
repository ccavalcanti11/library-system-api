![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.3-brightgreen.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-Enabled-green.svg)
![Redis](https://img.shields.io/badge/Redis-Caching-red.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)
![Build](https://github.com/ccavalcanti11/library-system-api/actions/workflows/build.yml/badge.svg)

RESTful API for a Library System

Welcome to the Library System API — a showcase of clean, modern Java 21 development using plain Java and Spring Boot. This project demonstrates my ability to build scalable, maintainable, and well-documented RESTful services with a focus on clarity, testing, and best practices.

🚀 Tech Stack

Java 21

Spring Boot

Spring Data MongoDB

Redis (Spring Data Redis)

Swagger/OpenAPI

JUnit 5 + Mockito

Gradle

Docker

📚 Features

CRUD operations for:

Books

Authors

Borrowers

Loans

Validation and error handling

RESTful design with proper HTTP status codes

API documentation via Swagger UI

Unit and integration tests for core components

MongoDB for persistent storage

Redis for caching frequently accessed data

📂 Project Structure

src/
├── main/
│   ├── java/com/librarysystem/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── model/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/librarysystem/
        ├── unit/
        └── integration/

🧪 Testing

Unit tests for services and controllers

Integration tests for repository and API endpoints

Mocking with Mockito

📖 API Documentation

Once the application is running, access the Swagger UI at:

http://localhost:8080/swagger-ui/index.html

🛠️ How to Run

Clone the repository:

git clone https://github.com/yourusername/library-system-api.git

Navigate to the project folder:

cd library-system-api

Run the application with Gradle:

./gradlew bootRun

🐳 Docker Setup

To run the application with MongoDB and Redis using Docker:

Build the Docker image:

docker build -t library-system-api .

Use Docker Compose to start the services:

version: '3.8'
services:
  mongo:
    image: mongo:latest
    ports:
      - "27017:27017"

  redis:
    image: redis:latest
    ports:
      - "6379:6379"

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mongo
      - redis
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/library
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379

Start the containers:

docker-compose up

📬 Contact

If you're a recruiter or developer interested in collaborating, feel free to reach out via LinkedIn or email me at your.email@example.com.

Thanks for checking out my project! ⭐ If you like it, consider starring the repo!
