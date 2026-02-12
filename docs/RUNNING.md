## Running the Food Delivery System

### Prerequisites

- Docker and Docker Compose installed
- Java 17 (for local development of services)
- Node.js 18+ and npm or pnpm (for frontend development)

### Quick Start (Docker Compose)

1. Build all backend services and frontend:
   - From the repository root, run:
     - `mvn -pl eureka-server,api-gateway,user-service,restaurant-service,order-service,payment-service,delivery-service,notification-service clean package -DskipTests`
     - `cd frontend && npm install && npm run build && cd ..`
2. Start infrastructure and services:
   - `docker-compose up --build`
3. Access:
   - API Gateway: `http://localhost:8080`
   - Frontend: `http://localhost:5173` (dev) or exposed container port (prod build)
   - Eureka Dashboard: `http://localhost:8761`

### Local Development

- Run MySQL and Kafka via `docker-compose` (comment out app services if needed).
- Start individual Spring Boot services from your IDE.
- Start the frontend in dev mode from `frontend` using `npm run dev`.

