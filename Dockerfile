# Етап 1: Збірка проекту
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Копіюємо всі файли проекту до контейнера
COPY . .

# Виконуємо збірку проекту
RUN ./mvnw clean package -DskipTests

# Етап 2: Запуск додатку
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копіюємо зібраний JAR-файл з попереднього етапу
COPY --from=build /app/target/*.jar app.jar

# Відкриваємо порт для програми
EXPOSE 8080

# Запускаємо програму
CMD ["java", "-jar", "app.jar"]