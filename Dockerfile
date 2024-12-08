# Використовуємо офіційний образ OpenJDK
FROM openjdk:16-jdk-alpine

# Встановлюємо робочу директорію
WORKDIR /app

# Копіюємо JAR файл додатку до контейнера
COPY target/chat-0.0.1-SNAPSHOT.jar app.jar

# Відкриваємо порт для Spring Boot
EXPOSE 8080

# Команда для запуску додатку
ENTRYPOINT ["java", "-jar", "app.jar"]
