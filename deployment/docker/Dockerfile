# FROM eclipse-temurin:17-jre as builder
FROM registry.gitlab.com/gaia-x/data-infrastructure-federation-services/por/demo/eclipse-temurin:11-jre-alpine as builder
WORKDIR extracted
ADD output/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM registry.gitlab.com/gaia-x/data-infrastructure-federation-services/por/demo/eclipse-temurin:11-jre-alpine
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 8092

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]