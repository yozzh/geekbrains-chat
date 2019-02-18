# Geekbrains Chat
## Основные возможности
* Регистрация пользователей при первой авторизации
* Валидация пользователей при регистрации
* Актуальное отображение списка пользователей с их статусом
* Форматирование сообщений в чате (своё/чужое сообщение)
* Валидация полей ввода настроек
* Сохранение ранее заданных полей

## Запуск

Перед началом работ требуется запустить миграции и автогенерацию классов для `Jooq`:
* `gradle generateSampleJooqSchemaSource` - запуск автогенерации классов ORM
* `gradle flywayMigrate` - запуск миграций для базы данных


В `gradle` описаны два основных таска для запуска приложения:
* `runServer` - запуск в режиме сервера
* `runClient` - запуск в режиме клиента