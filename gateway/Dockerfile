# Utilisez l'image de base Java
FROM maven:3.8.4-openjdk-17-slim



# Définir le répertoire de travail
WORKDIR /app


COPY pom.xml pom.xml
RUN mvn dependency:go-offline

COPY . .

CMD ["mvn", "spring-boot:run"]
#
## Copiez le fichier JAR de l'application dans le conteneur
#COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway-0.0.1-SNAPSHOT.jar
#
#
## Commande pour exécuter l'application Spring Boot lorsque le conteneur démarre
#CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]
