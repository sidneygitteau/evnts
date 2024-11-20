# Utilisez l'image de base Java 17 (correspondant à votre configuration dans le pom.xml)
FROM maven:3.8.4-openjdk-17-slim


# Définir le répertoire de travail
WORKDIR /app


COPY pom.xml pom.xml
RUN mvn dependency:go-offline

COPY . .

CMD ["mvn", "spring-boot:run"]