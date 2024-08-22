FROM openjdk:21-jdk-slim

WORKDIR /app

COPY importer/build/libs/importer-*.jar /app/importer.jar

RUN mkdir -p /app/camt/done
RUN mkdir -p /app/import/done

ENTRYPOINT ["java", "-jar", "importer.jar"]
