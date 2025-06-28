# GreenImpact — Backend

## О проекте

GreenImpact — backend часть веб-приложения для мотивации и отслеживания экологичных действий.  
Пользователи могут регистрироваться, участвовать в эко-событиях, накапливать баллы и получать награды.

## Технологии

- Java 17
- Spring Boot
- PostgreSQL
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

Зарегистрированные пользователи обладают всеми функциями, доступными незарегистрированным, а также получают дополнительные возможности:

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
http://localhost:8080/swagger-ui/index.html
```
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
1. Для работы с маршрутизацией необходимо использовать официальные образы OSRM. Чтобы скачать их локально, выполните команду:
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

Данный backend предназначен для взаимодействия с frontend-приложением, исходный код которого расположен в отдельном репозитории.
Для корректной работы и полноценного тестирования рекомендуется использовать оба репозитория совместно.

👉[Перейти к frontend-репозиторию](https://github.com/discovery126/green-impact-frontend)