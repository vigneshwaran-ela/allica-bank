README.txt
==========

Project: Allica Bank Customer Management API
Version: 1.0.0
Spring Boot: 3.4.5

Overview:
---------
This project is a RESTful microservice for managing customer data in a retailer-based context.
It provides endpoints for creating, reading, updating, and deleting customers.
Each customer is uniquely associated with a retailer, identified through a custom header (`X-RETAILER`).

Technologies:
-------------
- Java 17+
- Spring Boot 3.4.5
- Spring Data JPA
- Hibernate Validator
- Swagger / OpenAPI 3 (via springdoc-openapi)
- Jakarta Validation (JSR 380)
- Maven

Modules:
--------
1. **Controller Layer**: 
   Exposes REST endpoints under `/api/v1/customer`.

2. **Service Layer**:
   Handles business logic and interaction between controller and persistence layer.

3. **Repository Layer**:
   Interfaces for data access using Spring Data JPA.

4. **Validation**:
   Custom annotation `@UniqueLoginName` checks for login name uniqueness scoped to a retailer.

5. **Swagger UI**:
   Auto-generated API documentation accessible via:
   → http://localhost:8080/swagger-ui.html

Build and Run:
--------------
To build the project:
> mvn clean install

To run the project locally:
> mvn spring-boot:run

API Usage:
----------
All requests must include the `X-RETAILER` header (e.g., `X-RETAILER: RETAILER_A`).

Base URL: `/api/v1/customer`

| Method | Endpoint            | Description         |
|--------|---------------------|---------------------|

| POST   | `/`                 | Create a customer   |

| GET    | `/{id}`             | Get customer by ID  |

| PUT    | `/{id}`             | Update customer     |

| DELETE | `/{id}`             | Delete customer     |


Example cURL:
-------------
Create Customer:
curl -X POST http://localhost:8080/api/v1/customer \
  -H "Content-Type: application/json" \
  -H "X-RETAILER: RETAILER_A" \
  -H "X-API-KEY: password" \
  -d '{"firstName":"John","lastName":"Doe","loginName":"jdoe","dob":"1990-01-01"}'

API Documentation:
------------------
Once the application is running, visit:
→ http://localhost:8080/swagger-ui.html
→ http://localhost:8080/v3/api-docs (raw OpenAPI spec)

Configuration Notes:
--------------------
- Retailers are predefined and retrieved using `RetailerType.fromName(String name)`.
- Customer uniqueness is validated against retailer + login name combination.
- Application logs customer actions with traceable logs using SLF4J + MDC.

Customer API Working Retailer and API-Key:
---------------------
[
{
  "name" : "AMAZON",
  "apiKey" : "APIKEY_AMAZON_123"
},
{
  "name" : "FLIPKART",
  "apiKey" : "APIKEY_FLIPKART_456"
},
{
  "name" : "WALMART",
  "apiKey" : "APIKEY_WALMART_789"
}
]

Retailer API Working basic auth credentails:
---------------------
"userName" : "ups_admin"
"password" : "password123"


Contact:
--------

Email: er.vigneshwaran@outlook.com
