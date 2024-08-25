# Card Cost API

## Overview

The Card Cost API is a Spring Boot application that provides a RESTful interface for managing clearing costs based on card numbers. The API determines the clearing cost by using the Issuer Identification Number (IIN) or Bank Identification Number (BIN) extracted from a card number.

## Features

- **CRUD Operations**: Full Create, Read, Update, Delete operations on the clearing cost data.
- **Dynamic Clearing Cost Calculation**: Determine the clearing cost based on the card's country code.
- **BIN Lookup Integration**: Integrates with an external BIN lookup service to retrieve country information.
- **Caching**: Caches frequently accessed data to improve performance and reduce database load.
- **Fallback Mechanism**: Automatically falls back to a default "OTHERS" country code if a specific country is not found.
- **High Throughput Handling**: Designed to handle up to 7,000 API calls per minute with scalability options.
- **Swagger Integration**: Includes Swagger for easy API testing and documentation.

## Prerequisites

- Java 17
- Maven 3.6+

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/gino-alajar-ph/cardcost-api.git
cd cardcost-api
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

### Accessing the H2 Console

The application uses an in-memory H2 database for data storage. You can access the H2 console at:

```
http://localhost:8080/h2-console
```

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**:

### Swagger for API Testing

This project is integrated with Swagger, which provides an easy-to-use interface for testing the API endpoints. Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

Use Swagger to explore the API, execute requests, and see the expected responses. This is particularly useful for testing the different endpoints without needing an external tool.

## API Endpoints

### Retrieve Clearing Cost by Card Number

- **URL**: `/api/card-cost/payment-cards-cost`
- **Method**: `POST`
- **Request Body**: `String` (card number)
- **Response**:
  - `200 OK`: Returns the clearing cost and country code.
  - `404 Not Found`: If the BIN information cannot be retrieved or no clearing cost is found.

### Other Endpoints

- **CRUD operations** for managing clearing costs can be found and tested via Swagger.

## Configuration

Configuration is managed via the `application.yml` file located in `src/main/resources`. Key configurations include:

- **Database**: In-memory H2 database.
- **Swagger UI**: Enabled for easy API documentation and testing.

## Docker Setup

This application can be run using Docker, which also sets up a Redis container for caching purposes.

### Docker Compose

The provided `docker-compose.yml` file sets up the application and Redis in a connected network.

```yaml
version: "3.8"

services:
  redis:
    image: redis:latest
    container_name: redis-service
    ports:
      - "6379:6379"
    networks:
      - app-network

  app:
    build: .
    container_name: cardcost-api-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_REDIS_HOST=redis-service
      - SPRING_REDIS_PORT=6379
    networks:
      - app-network
    depends_on:
      - redis

networks:
  app-network:
    driver: bridge
```

### Running with Docker

To build and run the application with Docker:

```bash
docker-compose up --build
```

To stop and remove the containers:

```bash
docker-compose down
```

## Testing

### Unit Tests

The project includes unit tests for the core functionality. To run the tests, use:

```bash
mvn test
```

### Integration Tests

Swagger can be used for testing the API endpoints, as mentioned above. It's an easy and interactive way to validate the API functionality directly in your browser.

## Future Enhancements

- **Security Enhancements**: Implement HTTPS for secure communication, integrate OAuth2 for API authentication, and enforce stricter CORS policies.
- **High Availability**: Deploy the application using Kubernetes for better scalability and load balancing across multiple instances.
- **Extendability**: Incorporate asynchronous processing and reactive REST APIs using Spring WebFlux to handle a larger number of concurrent requests efficiently.

## Conclusion

This README provides a comprehensive guide to running the Card Cost API using Docker with an integrated Redis cache. It also includes pointers for future enhancements related to security, high availability, and extendability.