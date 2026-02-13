# Food Delivery System

A microservices-based Food Delivery Application built with Java Spring Boot, React, Kafka, Docker, and MySQL.

## Architecture

- **Service Registry**: Eureka Server (Port 8761)
- **API Gateway**: Spring Cloud Gateway (Port 8080)
- **Services**:
  - User Service (Auth)
  - Restaurant Service
  - Order Service
  - Payment Service
  - Delivery Service
  - Notification Service
- **Messaging**: Apache Kafka
- **Database**: 5x MySQL Containers
- **Frontend**: React + Vite (Port 5173)

## Prerequisites

- Docker Desktop installed and running
- Java 17+
- Node.js 18+

## How to Run

### 1. Start Infrastructure & Databases

```bash
docker-compose up -d
```
Wait about 30-60 seconds for MySQL and Kafka to initialize.

### 2. Build & Run Microservices

You can run them via IDE or Maven.

```bash
# In separate terminals or run all in background
./mvnw spring-boot:run -pl eureka-server
./mvnw spring-boot:run -pl api-gateway
./mvnw spring-boot:run -pl user-service
./mvnw spring-boot:run -pl restaurant-service
./mvnw spring-boot:run -pl order-service
./mvnw spring-boot:run -pl payment-service
./mvnw spring-boot:run -pl delivery-service
./mvnw spring-boot:run -pl notification-service
```
*Note: Ensure `infra/mysql/init/init.sql` ran successfully (docker logs food-mysql).*

### 3. Run Frontend

```bash
cd frontend
npm install
npm run dev
```
Access the UI at `http://localhost:5173`.

## API Documentation

Import `docs/postman_collection.json` into Postman to test the APIs.

## User Roles (for testing)

1. **Register** a user with role `RESTAURANT_OWNER` to create restaurants.
2. **Register** a user with role `CUSTOMER` to place orders.
3. **Register** a user with role `DELIVERY_AGENT` (optional, assignments are simulated).
