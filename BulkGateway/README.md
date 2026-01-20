# BulkGateway API - Spring Boot Application

A RESTful API for bulk message management built with Spring Boot 3.2.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/bulkgateway/
│   │       ├── BulkGatewayApplication.java     # Main Application Entry Point
│   │       ├── config/                          # Configuration Classes
│   │       │   ├── CorsConfig.java
│   │       │   └── OpenApiConfig.java
│   │       ├── controller/                      # REST Controllers
│   │       │   ├── HealthController.java
│   │       │   └── MessageController.java
│   │       ├── dto/                             # Data Transfer Objects
│   │       │   ├── ApiResponse.java
│   │       │   ├── MessageRequest.java
│   │       │   └── MessageResponse.java
│   │       ├── exception/                       # Exception Handling
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── model/                           # Entity Classes
│   │       │   └── Message.java
│   │       ├── repository/                      # Data Access Layer
│   │       │   └── MessageRepository.java
│   │       └── service/                         # Business Logic Layer
│   │           ├── MessageService.java
│   │           └── impl/
│   │               └── MessageServiceImpl.java
│   └── resources/
│       └── application.properties               # Application Configuration
└── test/
    └── java/
        └── com/example/bulkgateway/
            ├── BulkGatewayApplicationTests.java
            └── controller/
                └── MessageControllerTest.java
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Getting Started

### Build the Project

```bash
mvn clean package -DskipTests
```

### Run with H2 Database (No MySQL needed)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### Run with MySQL Database

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081/api`

## Insert Data via REST API

### Insert a Single Message (POST)

```bash
curl -X POST "http://localhost:8081/api/messages" \
  -H "Content-Type: application/json" \
  -d '{
    "sender": "john@example.com",
    "recipient": "jane@example.com",
    "content": "Hello, this is a test message!"
  }'
```

### Insert Multiple Messages (Bulk POST)

```bash
curl -X POST "http://localhost:8081/api/messages/bulk" \
  -H "Content-Type: application/json" \
  -d '[
    {"sender": "user1@test.com", "recipient": "user2@test.com", "content": "Message 1"},
    {"sender": "user3@test.com", "recipient": "user4@test.com", "content": "Message 2"}
  ]'
```

### Get All Messages (SELECT)

```bash
curl "http://localhost:8081/api/messages"
```

### Run Test Script

```bash
chmod +x test-api.sh
./test-api.sh
```

## Auto-Inserted Sample Data

When the application starts, it automatically inserts 5 sample messages into the database via the `DataInitializer` component.

## Old Build
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## API Endpoints

The API is available at `http://localhost:8080/api`

### Health Check
- `GET /` - Welcome message
- `GET /health` - Health check endpoint
- `GET /info` - Application info

### Messages
- `POST /messages` - Create a new message
- `GET /messages` - Get all messages
- `GET /messages/{id}` - Get message by ID
- `GET /messages/sender/{sender}` - Get messages by sender
- `GET /messages/recipient/{recipient}` - Get messages by recipient
- `GET /messages/status/{status}` - Get messages by status
- `PUT /messages/{id}` - Update a message
- `PATCH /messages/{id}/status?status=NEW_STATUS` - Update message status
- `DELETE /messages/{id}` - Delete a message
- `POST /messages/bulk` - Send bulk messages
- `GET /messages/count/status/{status}` - Get message count by status

## API Documentation

Swagger UI is available at: `http://localhost:8080/api/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`

## Database

### H2 Console (Development)
Access H2 database console at: `http://localhost:8080/api/h2-console`

- JDBC URL: `jdbc:h2:mem:bulkgatewaydb`
- Username: `sa`
- Password: (empty)

### MySQL (Production)
Update `application.properties` for MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bulkgatewaydb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Example API Usage

### Create a Message
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{
    "sender": "sender@example.com",
    "recipient": "recipient@example.com",
    "content": "Hello, this is a test message!"
  }'
```

### Get All Messages
```bash
curl http://localhost:8080/api/messages
```

### Send Bulk Messages
```bash
curl -X POST http://localhost:8080/api/messages/bulk \
  -H "Content-Type: application/json" \
  -d '[
    {
      "sender": "sender@example.com",
      "recipient": "user1@example.com",
      "content": "Message 1"
    },
    {
      "sender": "sender@example.com",
      "recipient": "user2@example.com",
      "content": "Message 2"
    }
  ]'
```

## Technologies Used

- **Spring Boot 3.2** - Application framework
- **Spring Data JPA** - Data access layer
- **H2 Database** - In-memory database for development
- **MySQL** - Production database
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **Spring Boot Actuator** - Application monitoring
- **JUnit 5** - Testing framework

## License

Apache License 2.0

