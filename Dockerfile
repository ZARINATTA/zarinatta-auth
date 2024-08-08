FROM openjdk:18-jre-alpine

ENV TZ=Asia/Seoul

WORKDIR /zarinatta-oauth

# 인자 설정 - JAR_File
ARG JAR_FILE=/build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 해당 포트를 외부로 개방
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]