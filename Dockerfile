FROM clojure:lein as build

WORKDIR /severino

ADD src src
ADD project.clj .

RUN lein uberjar

FROM openjdk:15-jdk-alpine3.11

WORKDIR /severino

COPY --from=build /severino/target/uberjar/severino-0.1.0-standalone.jar severino.jar

ENTRYPOINT [ "java", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "severino.jar" ]