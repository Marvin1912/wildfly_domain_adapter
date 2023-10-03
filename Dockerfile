FROM openjdk:17-jdk-slim

WORKDIR /app

COPY cert/server.crt /app/

COPY adapter_application/build/libs/adapter_application-*.jar /app/adapter_application.jar

RUN keytool -import -file /app/server.crt \
    -alias wildfly_domain \
    -trustcacerts \
    -cacerts \
    -storepass changeit \
    -noprompt

RUN mkdir -p /app/camt/done

ENTRYPOINT ["java", "-jar", "adapter_application.jar"]
