# ENGINEERING_NOTES.md

# URL Shortener API — Engineering Notes

This document captures the important engineering decisions, trade-offs, implementation choices, and lessons learned while building this project.

---

# Project Goal

Build a production-inspired URL Shortener that demonstrates modern backend engineering concepts instead of only implementing CRUD operations.

Primary focus areas:

* REST API Design
* Authentication & Authorization
* Asynchronous Processing
* AWS Integration
* Clean Architecture
* Database Design

---

# System Overview

The application provides:

* URL Shortening with Base62 encoding
* HTTP 302 Redirects with expiry validation
* JWT Authentication and Authorization
* User-specific URL ownership
* Asynchronous click analytics using AWS SQS
* QR Code generation using ZXing
* AWS S3 integration for QR code storage

---

# Architecture

```
Client
  ↓
POST /api/shorten (JWT Protected)
  ↓
UrlService
  ↓ ──────────────────────────────────┐
Base62Encoder                  QrCodeGenerator (ZXing)
  ↓                                   ↓
MySQL (urls table)             S3Service → AWS S3
  ↓
Response { shortUrl, qrCodeUrl }

GET /api/{shortCode} (Public)
  ↓
UrlService → MySQL
  ↓
ClickEventProducer → AWS SQS (Non-blocking)
  ↓                       ↓
HTTP 302 Redirect    ClickEventConsumer
                          ↓
                    MySQL (click_events table)
```

---

# Major Engineering Decisions

---

## 1. Base62 Encoding

### Problem

Generate short, readable URLs while minimizing collisions.

### Decision

Implemented Base62 encoding using a 62-character set
(a-z, A-Z, 0-9) to generate 6-character short codes.

### Why?

* Human-readable
* Compact identifiers
* Widely used in production URL shorteners

### Alternative Considered

Hashing the original URL.

### Why Not?

* Difficult collision management
* Longer identifiers
* Less readable URLs

---

## 2. JWT Authentication

### Problem

Users should only manage their own URLs.

### Decision

Implemented stateless JWT authentication using JJWT library
with Spring Security filter chain.

### Why?

* No server-side session storage required
* Scales horizontally
* Common production approach

### Trade-off

JWT invalidation is harder than session-based authentication.
Token cannot be revoked until expiry without a denylist.

---

## 3. URL Ownership

### Problem

Multiple users may shorten URLs — ownership needs to be tracked.

### Decision

Each shortened URL is linked to exactly one user via
a ManyToOne relationship in the database.

### Why?

Allows:

* User dashboard — GET /api/my-urls
* Delete API with ownership validation
* User-specific URL management

### Implementation Note

FetchType.LAZY used on User relationship to avoid
unnecessary JOIN queries on every URL fetch.

---

## 4. Asynchronous Analytics

### Problem

Analytics tracking should never slow down redirects.

### Decision

On every redirect, publish a click event to AWS SQS.
A separate SQS Consumer processes and saves events to DB.

### Why?

* Redirect remains instant — no DB write on critical path
* Analytics processing becomes completely independent
* SQS acts as a reliable buffer between producer and consumer

### Trade-off

Analytics becomes eventually consistent —
there is a small delay between redirect and analytics update.

---

## 5. AWS SQS

### Why SQS?

* Fully managed service — no infrastructure to maintain
* Reliable message delivery with retry support
* Loose coupling between redirect service and analytics

### Why Not Synchronous Database Writes?

Because redirect latency should remain as low as possible.
A DB write on every redirect would add unnecessary latency.

---

## 6. AWS S3

### Problem

QR codes are binary image files — storing them in MySQL
is inefficient and not scalable.

### Decision

* Generate QR code locally using ZXing library
* Upload PNG bytes directly to AWS S3
* Store only the public S3 URL in the response

### Flow

```
POST /api/shorten
      ↓
Base62 short code generated
      ↓
QR code generated (ZXing → byte[])
      ↓
byte[] uploaded to AWS S3
      ↓
Public S3 URL returned in response
```

### Note

QR generation wrapped in try-catch —
if S3 upload fails, URL shortening still succeeds.
This is graceful degradation.

---

# Security Decisions

* Passwords stored using BCrypt one-way hashing with salt
* JWT used for stateless authentication
* Spring Security filter chain validates token on every request
* Input validation using Jakarta Bean Validation (@Valid)
* Global exception handling via @RestControllerAdvice to avoid
  exposing internal stack traces
* AWS credentials managed via ~/.aws/credentials —
  never hardcoded in application properties

---

# Design Patterns Used

## Producer–Consumer Pattern

```
ClickEventProducer
      ↓
  AWS SQS
      ↓
ClickEventConsumer
```

### Reason

Loose coupling between redirect flow and analytics processing.
Producer and Consumer are completely independent —
failure in consumer never affects redirect.

---

# Database Design

### Tables

* users — id, email, password, created_at
* urls — id, original_url, short_code, created_at, expires_at, user_id
* click_events — id, short_code, ip_address, clicked_at

### Relationships

```
User
 1
 ↓
 N
URLs
 1
 ↓
 N
Click Events
```

---

# Challenges Faced

## Spring Security Filter Chain

Understanding filter ordering and integrating
JWT validation as a custom OncePerRequestFilter
before UsernamePasswordAuthenticationFilter.

---

## AWS Credentials

Configured AWS Default Credential Provider Chain correctly
for Spring Cloud AWS to pick up credentials from
~/.aws/credentials without hardcoding.

---

## Spring Boot Version Compatibility

Resolved dependency conflicts between Spring Boot version,
Spring Cloud AWS, Springdoc OpenAPI, and JJWT by
identifying and using compatible version combinations.

---

## S3 Public Access

AWS recently disabled ACLs by default on new buckets.
Resolved by using Bucket Policy for public read access
instead of ObjectCannedACL — which is the AWS recommended approach.

---

# Engineering Insights

* Designing layered applications is more important than
  writing controller code.

* JWT authentication requires understanding Spring Security's
  filter chain — not just token generation.

* Asynchronous messaging improves response time by separating
  analytics processing from request handling.

* Cloud services simplify infrastructure but require careful IAM configuration and credential management.

* Graceful degradation matters — non-critical features like
  QR generation should never break core functionality.
