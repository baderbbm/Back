FROM maven:3.8-openjdk-17

WORKDIR /app
COPY . /app

# Créez un fichier de configuration
RUN echo "spring.datasource.url=jdbc:mysql://host.docker.internal:3306/service?serverTimezone=UTC" > /app/application.properties
RUN echo "spring.datasource.username=root" >> /app/application.properties
RUN echo "spring.datasource.password=rootroot" >> /app/application.properties
RUN echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect" >> /app/application.properties
# Exécutez mvn install pour télécharger les dépendances
RUN mvn install

# Exécutez mvn package pour construire le JAR
RUN mvn -B package

VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
