# Simple-E-commerce-API-java-spring

Simple E-commerce API with Spring Boot and Java.

It is meant to be used only in a test environment.

For that purpose, it uses Stripe as the only payment gateway.

# Swagger UI

Navigate to http://localhost:8080/swagger-ui/index.html

# application.properties

```properties
spring.application.name=Simple E-commerce API
# MongoDB Configuration
spring.data.mongodb.uri=YOUR_MONGODB_URI
spring.data.mongodb.database=YOUR_COLLECTION
# Security
security.jwt.secret-key=YOUR_SECRET
security.jwt.expiration-time=3600000
# API docs
springdoc.api-docs.path=/api-docs
# Stripe
stripe.api.key=YOUR_API_KEY
stripe.endpoint.secret=YOUR_WEBHOOK_SECRET
```
