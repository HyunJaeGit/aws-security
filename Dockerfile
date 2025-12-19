# 1. Java 17 실행 환경(JRE) 경량 버전 사용
# FROM openjdk:17-jdk-slim

# 1-1. Java 17 실행 환경 (Amazon Corretto 사용 - AWS 환경에 최적화됨)
FROM amazoncorretto:17-alpine-jdk

# 2. 빌드된 JAR 파일의 위치를 변수로 지정
ARG JAR_FILE=build/libs/*.jar

# 3. JAR 파일을 컨테이너 내부로 복사 (이름은 app.jar로 통일)
COPY ${JAR_FILE} app.jar

# 4. 컨테이너가 시작될 때 실행할 명령어
# 외부 환경변수에서 포트를 받을 수 있게 설정 (기본값 8080)
# ENTRYPOINT ["java", "-jar", "/app.jar"]

# 4-1. 애플리케이션 실행
# server.port를 5000으로 고정하여 실행 (기존 설정 유지)
ENTRYPOINT ["java", "-Dserver.port=5000", "-jar", "/app.jar"]