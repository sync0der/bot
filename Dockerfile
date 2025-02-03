# Используем официальный OpenJDK 21
FROM openjdk:21-oracle

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем скомпилированный JAR-файл
COPY target/bot.jar bot.jar

# Запускаем Spring Boot приложение
ENTRYPOINT ["java", "-jar", "bot.jar"]
