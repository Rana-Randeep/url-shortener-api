[//]: # (# URL Shortener API)

[//]: # ()
[//]: # (A URL shortening service built with Java and Spring Boot.)

[//]: # ()
[//]: # (## Tech Stack)

[//]: # (- Java 21)

[//]: # (- Spring Boot 4.0.6)

[//]: # (- MySQL)

[//]: # (- Maven)

[//]: # ()
[//]: # (## Status)

[//]: # (🚧 In Progress)

# 🔗 URL Shortener API

A production-inspired URL Shortening service built with Java and Spring Boot, featuring async click analytics, QR code generation, and AWS cloud integration.

## 🚀 Features

- **URL Shortening** — Base62 encoding to generate unique short codes
- **Redirect** — HTTP 302 redirect with expiry validation
- **Async Click Analytics** — AWS SQS based async click tracking pipeline
- **QR Code Generation** — Auto-generated QR code for every short URL stored on AWS S3
- **User Authentication** — JWT based registration and login
- **URL Ownership** — Users can view and delete their own URLs
- **Input Validation** — Request validation with meaningful error responses
- **Global Exception Handling** — Centralized error handling with proper HTTP status codes
- **API Documentation** — Swagger UI with JWT auth support

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.x |
| Security | Spring Security + JWT |
| Database | MySQL |
| ORM | Spring Data JPA + Hibernate |
| Queue | AWS SQS |
| Storage | AWS S3 |
| QR Code | ZXing |
| Build Tool | Maven |
| API Docs | Swagger / OpenAPI 3 |

## 📐 Architecture Overview

```
Client
  ↓
POST /api/shorten  (JWT protected)
  ↓
UrlService
  ↓ ──────────────────────────────────────────┐
Base62Encoder                           QrCodeGenerator
  ↓                                           ↓
MySQL (urls table)                      S3Service → AWS S3
  ↓
Response { shortUrl, qrCodeUrl }

GET /api/{shortCode}  (public)
  ↓
UrlService → MySQL
  ↓
ClickEventProducer → AWS SQS
  ↓                       ↓
HTTP 302 Redirect    ClickEventConsumer
                          ↓
                    MySQL (click_events table)
```

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- MySQL
- AWS Account (SQS + S3)
- AWS CLI configured (`~/.aws/credentials`)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/Rana-Randeep/url-shortener-api.git
cd url-shortener-api/url-shortener-api
```

**2. Create MySQL database**
```sql
CREATE DATABASE url_shortener_db;
```

**3. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.cloud.aws.region.static=ap-south-1
spring.cloud.aws.sqs.queue.click-events=click-events-queue
spring.cloud.aws.s3.bucket-name=your-bucket-name

app.jwt.secret=your-secret-key-atleast-32-chars
app.jwt.expiration=86400000
```

**4. Run the application**
```bash
./mvnw spring-boot:run
```

**5. Access Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT | No |

### URL Management
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/shorten` | Shorten URL + generate QR | Yes |
| GET | `/api/{shortCode}` | Redirect to original URL | No |
| GET | `/api/my-urls` | Get all URLs of logged in user | Yes |
| DELETE | `/api/urls/{shortCode}` | Delete a URL | Yes |

### Analytics
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/analytics/{shortCode}` | Get click stats | Yes |

## 🔐 Security

- JWT based stateless authentication
- BCrypt password hashing
- Centralized exception handling
- AWS credentials managed via `~/.aws/credentials` — never hardcoded

## 🗃️ Database Schema

```
users          → id, email, password, created_at
urls           → id, original_url, short_code, created_at, expires_at, user_id
click_events   → id, short_code, ip_address, clicked_at
```

## 📊 Async Analytics Flow

```
User visits short URL
        ↓
HTTP 302 Redirect (instant)
        ↓
Click event published to AWS SQS (non-blocking)
        ↓
SQS Consumer processes event in background
        ↓
Click saved to MySQL click_events table
```

> Redirect and analytics are fully decoupled — analytics failure never affects redirect performance.

## 🤝 Author

**Randeep** — 4th Year CS Student  
[GitHub](https://github.com/Rana-Randeep)