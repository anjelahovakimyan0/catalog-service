FROM openjdk:8-jdk-alpine
LABEL authors="Tornado"

ENTRYPOINT ["top", "-b"]