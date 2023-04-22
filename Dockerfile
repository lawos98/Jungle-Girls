FROM gradle:jdk17
WORKDIR /server
COPY server/build.gradle.kts settings.gradle.kts ./
COPY server/src ./src
RUN gradle build --no-daemon
EXPOSE 8080
CMD ["gradle", "bootRun"]