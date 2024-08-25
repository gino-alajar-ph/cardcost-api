# Card Cost API

## Overview

The Card Cost API is a Spring Boot application that provides a RESTful interface for managing clearing costs based on card numbers. The API determines the clearing cost by using the Issuer Identification Number (IIN) or Bank Identification Number (BIN) extracted from a card number.

## Features

- **CRUD Operations**: Full Create, Read, Update, Delete operations on the clearing cost data.
- **Dynamic Clearing Cost Calculation**: Determine the clearing cost based on the card's country code.
- **BIN Lookup Integration**: Integrates with an external BIN lookup service to retrieve country information.
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

## Testing

### Unit Tests

The project includes unit tests for the core functionality. To run the tests, use:

```bash
mvn test
```

### Integration Tests

Swagger can be used for testing the API endpoints, as mentioned above. It's an easy and interactive way to validate the API functionality directly in your browser.