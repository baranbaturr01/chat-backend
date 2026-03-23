# Chat App - Backend (Spring Boot)

## Proje Açıklaması
Müslüman topluluklar için gerçek zamanlı mesajlaşma uygulamasının backend API servisi.

## Teknoloji Stack
- Java 21 + Spring Boot 3.x
- PostgreSQL 16
- Spring WebSocket (STOMP)
- Spring Security + JWT (jjwt)
- Spring Data JPA / Hibernate
- Flyway (DB migration)
- Maven
- Swagger / SpringDoc OpenAPI
- Lombok
- MapStruct (DTO mapping)

## Mimari Kurallar
- Clean Architecture: Controller → Service → Repository
- Tüm endpoint'ler için Swagger/OpenAPI dokümantasyonu yaz
- Her entity için DTO kullan (Request DTO, Response DTO)
- Exception handling GlobalExceptionHandler ile yap
- Tüm mesajlar UTF-8 ve çoklu dil desteği olsun

## Paket Yapısı
```
src/main/java/com/chatapp/
├── config/          # Security, WebSocket, CORS config
├── controller/      # REST endpoints
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── enums/           # Enum types
├── exception/       # Custom exceptions + GlobalExceptionHandler
├── mapper/          # MapStruct mappers
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter, provider, utils
├── service/         # Business logic
│   └── impl/        # Service implementations
└── websocket/       # WebSocket handlers, interceptors
```

## Veritabanı Kuralları
- Flyway migration kullan
- Entity isimlendirme: snake_case
- Soft delete kullan (is_deleted flag)
- Audit fields: created_at, updated_at, created_by
- UUID primary keys

## API Standartları
- RESTful naming convention
- Pagination: Page/Size parametreleri
- Response wrapper: ApiResponse<T>
- HTTP status codes doğru kullanılmalı
- Validation: @Valid + custom validators

## Güvenlik
- BCrypt password hashing
- JWT access + refresh token
- CORS configuration
- Rate limiting (opsiyonel)
- Input sanitization