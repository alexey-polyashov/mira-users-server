# mira-users-server
Сервис для управления справочником пользователей

Микросервис, реализует
- CRUD операции со справочником пользователей
- CRUD операции с ролеми пользователей
- Сервси авторизации OAuth

Сервис работает на порту 8080, адрес сервиса после запуска приложения - http://localhost:8080/mira-users/

API сервиса доступно для просмотра через swagger - http://localhost:8080/mira-users/swagger-ui.html

Есть опубликованный API - https://mira-user-service.herokuapp.com/swagger-ui.html

# Инструкция по запуску

## 1 Варинат - запуск как отдельного приложения

1. Склонировать репозиторий к себе на компьютер
2. Открыть проект в среде разработки
3. Указать имя профиля для спринга через переменную среды: spring.profiles.active=dev
4. Запустить приложение - http://localhost:8080/mira-users/swagger-ui.html


## 2 Вариант - запуск в docker контейнере

1. !!! Сначала необходимо склонировать репозиторий: https://github.com/alexey-polyashov/mira-user-client и выполнить инструкции для создания образа в docker !!!
2. Далее склонировать текущий репозиторий к себе на компьютер
4. Открыть проект в среде разработки
5. Указать имя профиля для спринга через переменную среды: spring.profiles.active=dev
6. В среде разработки выполнить команду install в maven, для получения jar файла в папке target: ./mvnw clean install -DskipTests
7. На компьютере должен быть установлен Docker (ссылка для установки - https://www.docker.com/get-started/) 
8. В терминале выполнить команду: docker-compose up -d
9. Приложение запущено и доступно по адресу http://localhost:8081/mira-users-gateway/
10. Для работы с API можно использовать swagger - http://localhost:8081/mira-users-gateway/swagger-ui.html
11. Для остановки приложения выполнить команду docker-compose down
