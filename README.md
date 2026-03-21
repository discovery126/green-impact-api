# GreenImpact — Backend

## О проекте

GreenImpact — backend часть веб-приложения для мотивации и отслеживания экологичных действий.  
Пользователи могут регистрироваться, участвовать в эко-событиях, накапливать баллы и получать награды.

## Архитектура системы

Backend является частью веб-приложения GreenImpact. Он предоставляет REST API для фронтенда.

Основные компоненты:

- **Spring Boot** — бизнес-логика, обработка запросов, авторизация
- **PostgreSQL** — хранение данных пользователей, заданий, наград и мероприятий
- **VK Cloud** — хранение изображений заданий
- **OSRM** — сервис маршрутизации для построения маршрутов к мероприятиям (пеший / на машине)

Аутентификация пользователей реализована с использованием JWT через Spring Security.

Архитектура backend построена по принципу слоистой структуры:
**Controller → Service → Repository → Database**

### Диаграмма развертывания

![Диаграмма развертывания](docs/deployment-diagram.svg)

## Технологии

- Java 17
- Spring Boot
- PostgreSQL
- Flyway
- JWT
- Docker
- VK Cloud

## Требования

- Java 17+
- Maven 3+
- PostgreSQL 16+
- Docker

## Функциональные требования

### Незарегистрированный пользователь

- Возможность регистрации в системе через email и пароль
- Просмотр всех эко-заданий
- Просмотр всех наград
- Просмотр рейтинга пользователей
- Просмотр всех мероприятий

### Зарегистрированный пользователь

Зарегистрированные пользователи обладают всеми функциями, доступными незарегистрированным, а также получают
дополнительные возможности:

- Вход в систему через email и пароль
- Выбор эко-задания из списка (сортировка мусора, субботник, отказ от пластика и т. д.)
- Отправка выполненного эко-действия с подтверждением (например, фото)
- Начисление баллов за подтвержденные эко-действия
- Просмотр личной информации
- Просмотр истории выполненных заданий
- Просмотр полученных наград
- Обмен баллов на награды
- Возможность записаться на мероприятие

### Администратор

- Модерация подтверждений выполненных эко-поступков
- Управление эко-заданиями (добавление, изменение, удаление)
- Управление мероприятиями (добавление, редактирование, удаление)

## Документация API (Swagger)

Для удобства тестирования и ознакомления с API в проекте доступен Swagger UI.
После запуска приложения интерфейс документации будет доступен по адресу:

```bash
http://localhost:8080/api/swagger-ui/index.html
```

## Примеры использования API

### Регистрация пользователя

**HTTP Request:**  
`POST http://localhost:8080/api/v1/register`  
**Headers:** `Content-Type: application/json`

**Request Body:**

```json
{
  "email": "user@gmail.com",
  "password": "Password1!"
}
```

**Response Body:** (пусто)

Status Code: 201 Created

### Авторизация

**HTTP Request:**  
`POST http://localhost:8080/api/v1/login`  
**Headers:** `Content-Type: application/json`

**Request Body:**

```json
{
  "email": "test@test.ru",
  "password": "Passw0rd!"
}
```

**Response Body:**

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlkIjoxNywic3ViIjoidGVzdEB0ZXN0LnJ1IiwiaWF0IjoxNzczMzM4ODExLCJleHAiOjE3NzMzNTA4MTF9.YXBRKwiQx1UBx7cWZGY1lHCn0BZBKs8RxJV2ROEgkFs"
  }
}
```

Status Code: 200 OK

### Получение всех заданий

`GET http://localhost:8080/api/v1/tasks`

**Response Body:**

```json
{
  "code": 200,
  "data": [
    {
      "id": 22,
      "title": "Сдача батареек",
      "description": "Сдавайте использованные батарейки в специальные контейнеры. Это предотвращает загрязнение почвы и воды тяжёлыми металлами. Маленькое действие — большой вклад в природу.",
      "points": 8,
      "task_type": "DAILY",
      "expired_date": null,
      "category": {
        "id": 1,
        "category_name": "Сдача"
      }
    },
    {
      "id": 23,
      "title": "Проезд на велосипеде",
      "description": "Оставьте автомобиль дома и отправляйтесь на велосипеде. Снижайте выбросы углекислого газа и ведите активный образ жизни. Доступно один раз в день при подтверждении.",
      "points": 5,
      "task_type": "DAILY",
      "expired_date": null,
      "category": {
        "id": 2,
        "category_name": "Транспорт"
      }
    }
  ]
}
```

Status Code: 200 OK

## Нагрузочное тестирование

### Окружение

Тестирование проводилось на локальной машине (Intel Core i5, 16 GB RAM, Docker).

### Параметры

- **Инструмент:** Locust
- **Сценарий:** login → rewards → user → events → exchange
- **Нагрузка:** 10 → 700 пользователей (шаг — 2 мин)
- **Длительность:** ~10 минут

### Результаты

#### Сводка по endpoint'ам

![Метрики](docs/load-testing/endpoints.png)

- Avg: **~630–670 ms**
- Median: **~290–340 ms**

#### Динамика нагрузки

![Графики](docs/load-testing/charts.png)

- До **~300 RPS**
- Ошибки: **0.14%**
- Рост 95-го перцентиля при высокой нагрузке

### Вывод

Система стабильно обрабатывает нагрузку до **300 RPS** при **700 пользователях**  
**(верхняя граница теста из-за ограничений окружения).**  
Основное ухудшение — увеличение времени ответа под нагрузкой.

## Установка и запуск

### Сборка и настройка backend-приложения

1. Cклонируйте репозиторий

```bash
git clone https://github.com/discovery126/green-impact-api.git
cd green-impact-api
```

2. Создайте файл .env в корне проекта со следующими основными параметрами:

```bash
PORT=8080

JWT_SECRET=ваш_секретный_ключ
JWT_LIFETIME_ACCESS=12000000

DATABASE_URL=jdbc:postgresql://postgres:5432/green-impact
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=password

OPEN_CAGE_API=ваш_ключ_OpenCage

# Параметры доступа к VK Cloud
S3_ACCESS_KEY=ваш_S3_access_key
S3_SECRET_KEY=ваш_S3_secret_key
S3_BUCKET_NAME=название_бакета
```

3. Соберите приложение и создайте Docker-образ:

```bash 
mvn clean install -DskipTests
docker build -t app .
```

### Настройка OSRM (маршрутизация)

1. Для работы с маршрутизацией необходимо использовать официальные образы OSRM. Чтобы скачать их локально, выполните
   команду:

```bash
docker pull osrm/osrm-backend
```

2. Скачайте PBF-файл нужного региона, например, с [download.geofabrik.de](http://download.geofabrik.de).
3. Создайте две папки:

```bash
C:/green-impact-osrm/foot
C:/green-impact-osrm/car
```

4. Поместите PBF-файл в обе папки и переименуйте его в region.osm.pbf
5. Подготовьте данные OSRM для каждого профиля, выполнив команды:

```bash
# Для профиля foot
docker run --rm -v $(pwd)/foot:/data osrm/osrm-backend osrm-extract -p /opt/foot.lua /data/region.osm.pbf
docker run --rm -v $(pwd)/foot:/data osrm/osrm-backend osrm-partition /data/region.osrm
docker run --rm -v $(pwd)/foot:/data osrm/osrm-backend osrm-customize /data/region.osrm

# Для профиля car
docker run --rm -v $(pwd)/car:/data osrm/osrm-backend osrm-extract -p /opt/car.lua /data/region.osm.pbf
docker run --rm -v $(pwd)/car:/data osrm/osrm-backend osrm-partition /data/region.osrm
docker run --rm -v $(pwd)/car:/data osrm/osrm-backend osrm-customize /data/region.osrm
```

6. Запустите контейнеры OSRM с разными портами:

```bash
docker run -d -p 5000:5000 -v $(pwd)/foot:/data osrm/osrm-backend osrm-routed --algorithm mld /data/region.osrm
docker run -d -p 5001:5000 -v $(pwd)/car:/data osrm/osrm-backend osrm-routed --algorithm mld /data/region.osrm
```

### Запуск docker-compose

```bash
docker-compose up
```

## ℹ️ Примечание

Данный backend предназначен для взаимодействия с frontend-приложением, исходный код которого расположен в отдельном
репозитории.
Для корректной работы и полноценного тестирования рекомендуется использовать оба репозитория совместно.

👉[Перейти к frontend-репозиторию](https://github.com/discovery126/green-impact-frontend)