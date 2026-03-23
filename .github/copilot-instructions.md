# Chat App - Backend (Spring Boot)

## Proje Açıklaması
Müslüman topluluklar için gerçek zamanlı mesajlaşma uygulamasının backend API servisi.

## Teknoloji Stack
- Java 21 + Spring Boot 3.x
- PostgreSQL 16
- Spring WebSocket (STOMP)
- Spring Security + JWT
- Maven
- Flyway (DB migration)
- Swagger/OpenAPI (API dokümantasyonu)

## Mimari Kurallar
- Clean Architecture kullan (Controller → Service → Repository)
- Tüm endpoint'ler için Swagger/OpenAPI dokümantasyonu yaz
- Her entity için DTO kullan
- Exception handling GlobalExceptionHandler ile yap
- Tüm mesajlar UTF-8 ve çoklu dil desteği olsun

## Veritabanı
- Flyway migration kullan
- Entity isimlendirme: snake_case
- Soft delete kullan (is_deleted flag)

## Paket Yapısı
```
src/main/java/com/chatapp/
├── config/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── exception/
├── repository/
├── security/
├── service/
│   └── impl/
└── websocket/
```

## Güvenlik
- JWT tabanlı authentication
- BCrypt ile password hashing
- CORS yapılandırması
- Rate limiting

## API Standartları
- RESTful endpoint'ler
- Standart response wrapper kullan
- Pagination desteği
- Validation annotation'ları kullan
