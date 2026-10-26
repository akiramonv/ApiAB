# Provider API Emulator

Проект на Spring Boot для эмуляции API-запросов к базе данных биллинга, к которой нет доступа
у основного приложения. Запрос услуг, категорий, провайдеров, счетов, комиссий, организаций,
пользователей, ролей, специализаций и платежей.

## Подключение к БД

Работает поверх оригинальной БД `billing`:

- URL: `jdbc:postgresql://localhost:5432/billing`
- user: `postgres`
- schema: `public`
- `ddl-auto: validate` — Hibernate только сверяет маппинг со схемой и ничего не создаёт.

Важная настройка — `spring.jpa.hibernate.naming.physical-strategy: PhysicalNamingStrategyStandardImpl`.
Без неё Spring Boot переводит имена колонок в snake_case, а в `billing` они написаны как есть
(`Inn`, `TotalSum`, `RelatedProvider`, `CommissionId`), из-за чего валидация схемы падает.
Колонки с заглавными буквами в сущностях экранированы обратными кавычками: ``@Column(name = "`Inn`")``.

## Особенности схемы billing, заложенные в код

| Что | Как в БД | Как в API |
|-----|----------|-----------|
| Первичные ключи | `varchar`: у старых строк числа (`"1"`, `"45"`), у новых — UUID строкой | `String`, новые записи создаются в формате UUID |
| Суммы услуг | `provider_services.summ` / `ServicePrice` / `totalSumm` — строка в сомах, разделитель `.` или `,` | `BigDecimal` в сомах |
| Суммы платежей | `payment.sum` / `Commission` / `TotalSum` — строка целых **тыйынов**, у `sum` возможно значение `any` | `BigDecimal` в сомах, `any` → `sum: null` + `anySum: true` |
| Транзакции | `payment_transacts.sum_trn` — `integer` в тыйынах | `BigDecimal` в сомах |
| Роли | `roles.name` — открытый список (`admin`, `provider_user`, `superadmin`, `cashier`, `bank_office`) | `String`, не enum |
| Уровень комиссии | Отдельной таблицы нет: строка в `providers.commissionlevel` | enum `ProviderLevel` / `AccountLevel` / `ServiceLevel` |
| Тип комиссии | `commission.name` (`percent`/`fixed`), величина — `commission.value` строкой | `commissionType` + `commissionValue` |
| ИНН и ФИО плательщика | Массив `payment.source_check_db` (jsonb) вида `[{"key":"ИНН",...},{"key":"FIO",...}]` | поля `inn`, `fio` + полный список `checkFields` |
| Провайдер платежа | Прямой ссылки нет, только `payment.provider_user` → `users.provider` | `providerId` / `providerName` вычисляются через автора |
| Счета | Таблица `account_type` (в старой схеме эмулятора была `accounts`) | `/api/accounts` |
| Категории | Таблица `provider_service_categoryes`, привязка к провайдеру через `RelatedProvider` | `/api/categories` |

### Удаление

У `provider_services`, `provider_service_categoryes`, `specializations`, `users` и `payment`
в схеме есть флаг удаления, и на эти строки ссылаются другие таблицы. Поэтому `DELETE`
по этим сущностям выставляет флаг, а не удаляет строку. Списки такие записи не показывают;
чтобы увидеть их, добавьте `?includeDeleted=true`. Получение по id работает всегда.

`providers`, `roles`, `commission`, `account_type` и `organisations` удаляются физически;
если на строку кто-то ссылается, вернётся `409` с текстом нарушенного внешнего ключа.

### Таблицы, которые не используются

В маппинг не включены `requestresponses` (лог запросов), `Instructions`,
`__EFMigrationsHistory`, а также пустые `provider_collumns` и `provider_collumn_values`.
`BankFilials` подключена только на чтение — её название подставляется в ответ по провайдеру.

## Запуск

```bash
./gradlew bootRun
```

Нужен JDK 17 или 21 (`JAVA_HOME`); на более новых JDK Gradle 8.12 не стартует.
Порт по умолчанию — `2004` (задаётся в `src/main/resources/application.yml`, ключ `server.port`).

## Тестовые данные

`sql/test-data.sql` полностью очищает бизнес-таблицы `billing` и заполняет их
вымышленным набором: 3 организации, 6 провайдеров, 11 категорий, 21 услуга
(одна помечена удалённой), 6 счетов, 5 комиссий, 4 специализации, 5
пользователей (без общесистемного администратора) и 5 платежей во всех четырёх статусах.

```bash
psql -U postgres -d billing -f sql/test-data.sql
```

Схема при этом не меняется, поэтому `ddl-auto: validate` проходит.

Набор специально покрывает крайние случаи:

| Что проверяется | Где |
|---|---|
| `commissionlevel` = ProviderLevel / AccountLevel / ServiceLevel | провайдеры `prov-water`, `prov-telecom`, `prov-city` |
| Комиссия percent и fixed | `com-pct-1`, `com-fix-10` |
| `RequireInnForPayment` = true | `prov-heat`, `prov-city` |
| Услуга с ценой и с вводом суммы | `svc-water-2` и `svc-water-1` |
| Мягкое удаление (`?includeDeleted=true`) | услуга `svc-old-1` |
| Вложенная категория | `cat-gov-docs` → `cat-gov` |
| Все статусы платежа | `pay-demo-1..5` |
| Частичная оплата и ошибка транзакции | `payment_transacts` |

Учётные записи (пароли SHA-256, как в рабочей базе):

| Email | Пароль | Роль |
|---|---|---|
| `admin@abilling.kg` | `admin123` | admin (Демо-Водоканал) |
| `city@abilling.kg` | `city123` | admin (Демо-Мэрия) |
| `kassir@abilling.kg` | `kassir123` | provider_user |
| `operator@abilling.kg` | `operator123` | provider_user |
| `bank@abilling.kg` | `bank123` | provider_user |

Назначены только роли `admin` и `provider_user` — киоск работает именно с ними.
Строки `superadmin`, `cashier` и `bank_office` остаются в справочнике `roles`
(они принадлежат биллингу), но никому не назначены: терминал их не показывает,
не даёт назначить и прав по ним не выдаёт.

## Основные endpoints

### Services (`provider_services`)
- `GET /api/services` (`?includeDeleted=true`)
- `GET /api/services/{id}`
- `GET /api/services/by-category/{categoryId}`
- `GET /api/services/by-provider/{providerId}`
- `GET /api/services/by-account/{accountId}`
- `GET /api/services/by-user/{userId}` — по специализациям пользователя
- `GET /api/services/{id}/qr` — PNG: сохранённый `QrCodeBase64` либо код, сгенерированный из `qr_link`
- `POST /api/services`, `PUT /api/services/{id}`, `DELETE /api/services/{id}`

### Categories (`provider_service_categoryes`)
- `GET /api/categories` (`?includeDeleted=true`)
- `GET /api/categories/{id}`
- `GET /api/categories/by-provider/{providerId}`
- `GET /api/categories/by-parent/{parentId}`
- `POST` / `PUT /api/categories/{id}` / `DELETE /api/categories/{id}`

### Providers, Organisations, BankFilials
- `GET /api/providers`, `GET /api/providers/{id}`, `GET /api/providers/by-organisation/{organisationId}`
- `POST` / `PUT` / `DELETE /api/providers/{id}`
- `GET /api/organisations`, `GET /api/organisations/{id}`, `POST` / `PUT` / `DELETE`
- `GET /api/bank-filials`, `GET /api/bank-filials/{id}`

### Specializations, Users, Roles
- `GET /api/specializations` (`?includeDeleted=true`), `/{id}`, `/by-provider/{providerId}`, `POST` / `PUT` / `DELETE`
- `GET /api/users` (`?includeDeleted=true`), `/{id}`, `/by-provider/{providerId}`, `/by-specialization/{specialId}`, `POST` / `PUT` / `DELETE`
- `GET /api/roles`, `/{id}`, `/by-user/{userId}`, `POST` / `PUT` / `DELETE`

### Accounts (`account_type`) и комиссии
- `GET /api/accounts`, `/{id}`, `/by-provider/{providerId}`, `POST` / `PUT` / `DELETE`
- `GET /api/commissions`, `/{id}`, `POST` / `PUT` / `DELETE`
- `GET /api/commission-levels`, `GET /api/commission-levels/{name}` — только чтение, таблицы в БД нет

### Payments (`payment`, `payment_services`, `payment_transacts`)
- `GET /api/payments` (`?includeDeleted=true`)
- `GET /api/payments/{id}`
- `GET /api/payments/by-provider/{providerId}`
- `GET /api/payments/by-user/{userId}`
- `GET /api/payments/by-status/{status}` — `in_process`, `success`, `warning`, `closed`
- `GET /api/payments/by-paycode/{paycode}`
- `POST /api/payments`
- `PATCH /api/payments/{id}/status`
- `GET /api/payments/{id}/transacts`, `POST /api/payments/{id}/transacts`
- `GET /api/payments/{id}/qr`
- `DELETE /api/payments/{id}`

Создание платежа:

```json
{
  "providerUserId": "1",
  "serviceIds": ["067cd9f9-f67f-4183-98c4-ed33892d254c"],
  "inn": "11122233344455",
  "fio": "Тест Тестович",
  "clientType": "individual"
}
```

Что делает эмулятор при создании:

- сумма берётся из фиксированных сумм переданных услуг, иначе из поля `sum`
  (`"anySum": true` — платёж на произвольную сумму, в БД уходит `sum = "any"`);
- комиссия считается по уровню из `providers.commissionlevel`: `fixed` — как есть,
  `percent` — процент от суммы; можно переопределить полем `commission`;
- `paycode` берётся из запроса, иначе из `account_type.paymentcode`, иначе генерируется;
- `ИНН` и `FIO` пишутся в `source_check_db`, туда же попадают произвольные `checkFields`;
- в `payment_services` создаются строки по каждой услуге.

Транзакция без указания суммы гасит остаток; когда разовый платёж оплачен полностью,
он автоматически переходит в статус `success`.

### Универсальный endpoint

`POST /api/emulator/execute`

```json
{
  "operation": "SERVICES_BY_CATEGORY",
  "params": { "categoryId": "3" }
}
```

Поддерживаемые операции:

`ALL_SERVICES`, `SERVICE_BY_ID`, `SERVICES_BY_CATEGORY`, `SERVICES_BY_PROVIDER`,
`SERVICES_BY_ACCOUNT`, `SERVICES_BY_USER_SPECIALIZATION`, `CREATE_SERVICE`, `UPDATE_SERVICE`,
`ALL_CATEGORIES`, `CATEGORY_BY_ID`, `CATEGORIES_BY_PROVIDER`, `CATEGORIES_BY_PARENT`, `CREATE_CATEGORY`,
`ALL_PROVIDERS`, `PROVIDER_BY_ID`, `PROVIDERS_BY_ORGANISATION`, `CREATE_PROVIDER`,
`ALL_ORGANISATIONS`, `ORGANISATION_BY_ID`, `CREATE_ORGANISATION`, `ALL_BANK_FILIALS`,
`ALL_SPECIALIZATIONS`, `SPECIALIZATION_BY_ID`, `SPECIALIZATIONS_BY_PROVIDER`, `CREATE_SPECIALIZATION`,
`ALL_USERS`, `USER_BY_ID`, `USERS_BY_PROVIDER`, `USERS_BY_SPECIALIZATION`, `CREATE_USER`,
`ALL_ROLES`, `ROLE_BY_ID`, `ROLES_BY_USER`, `CREATE_ROLE`,
`ALL_ACCOUNTS`, `ACCOUNT_BY_ID`, `ACCOUNTS_BY_PROVIDER`, `CREATE_ACCOUNT`,
`ALL_COMMISSIONS`, `COMMISSION_BY_ID`, `CREATE_COMMISSION`, `ALL_COMMISSION_LEVELS`, `COMMISSION_LEVEL_BY_NAME`,
`ALL_PAYMENTS`, `PAYMENT_BY_ID`, `PAYMENTS_BY_PROVIDER`, `PAYMENTS_BY_USER`, `PAYMENTS_BY_STATUS`,
`PAYMENTS_BY_PAYCODE`, `CREATE_PAYMENT`, `UPDATE_PAYMENT_STATUS`, `ADD_PAYMENT_TRANSACT`, `PAYMENT_TRANSACTS`.

## Формат ответа

```json
{ "success": true, "data": { }, "message": "..." }
```

Коды ошибок: `400` — некорректный запрос или неизвестная операция, `404` — записи нет,
`409` — на строку ссылаются другие таблицы, `500` — прочее.
