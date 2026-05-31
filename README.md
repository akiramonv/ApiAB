# Provider API Emulator

Проект на Spring Boot для эмуляции API-запросов к базе данных к которой нету доступа у основного приложения. Запрос услуг, категорий, счетов, комиссий, пользователей, ролей и специализаций.

## Что реализовано

- получение всех услуг;
- получение одной услуги;
- получение одной категории;
- получение всех категорий;
- услуги по категории;
- услуги по провайдеру;
- услуги, доступные пользователю по специализации;
- CRUD для услуг, категорий, провайдеров, пользователей и специализаций;
- универсальный endpoint для запросов сторонних сервисов.

## Подключение к вашей PostgreSQL БД

По умолчанию так, потому что у меня так:

- URL: `jdbc:postgresql://localhost:5432/parodiaMisera`
- user: `postgres`
- schema: `public`

Если таблицы уже созданы, `ddl-auto` стоит в режиме `validate`, чтобы Hibernate не пытался пересоздавать схему.

## Запуск

```bash
./gradlew bootRun
```

## Основные endpoints

### Services
- `GET /api/services`
- `GET /api/services/{id}`
- `GET /api/services/by-category/{categoryId}`
- `GET /api/services/by-provider/{providerId}`
- `GET /api/services/by-user/{userId}`
- `POST /api/services`
- `PUT /api/services/{id}`
- `DELETE /api/services/{id}`

### Categories
- `GET /api/categories`
- `GET /api/categories/{id}`
- `GET /api/categories/by-provider/{providerId}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Providers
- `GET /api/providers`
- `GET /api/providers/{id}`
- `POST /api/providers`
- `PUT /api/providers/{id}`
- `DELETE /api/providers/{id}`

### Specializations
- `GET /api/specializations`
- `GET /api/specializations/{id}`
- `GET /api/specializations/by-provider/{providerId}`
- `POST /api/specializations`
- `PUT /api/specializations/{id}`
- `DELETE /api/specializations/{id}`

### Users
- `GET /api/users`
- `GET /api/users/{id}`
- `GET /api/users/by-provider/{providerId}`
- `GET /api/users/by-specialization/{specialId}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Universal emulator endpoint
`POST /api/emulator/execute`

Пример:
```json
{
  "operation": "SERVICES_BY_CATEGORY",
  "params": {
    "categoryId": "11111111-1111-1111-1111-111111111111"
  }
}
```

Поддерживаемые операции:
- `ALL_SERVICES`
- `SERVICE_BY_ID`
- `SERVICES_BY_CATEGORY`
- `SERVICES_BY_PROVIDER`
- `SERVICES_BY_USER_SPECIALIZATION`
- `ALL_CATEGORIES`
- `CATEGORY_BY_ID`
- `ALL_PROVIDERS`
- `PROVIDER_BY_ID`
- `ALL_SPECIALIZATIONS`
- `SPECIALIZATION_BY_ID`
- `ALL_USERS`
- `USER_BY_ID`
- `USERS_BY_PROVIDER`
- `USERS_BY_SPECIALIZATION`
