FROM openjdk:17-jdk-slim

WORKDIR /app

COPY cert/server.crt /app/

COPY adapter_application/build/libs/importer-*.jar /app/importer.jar

RUN mkdir -p /app/camt/done
RUN mkdir -p /app/import/done

ENTRYPOINT ["java", "-jar", "importer.jar"]
