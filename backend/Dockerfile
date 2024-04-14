# Debian 기반 OpenJDK 이미지를 사용
FROM openjdk:17-bullseye

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    xvfb \
    libxi6 \
    gdebi-core \
    libglib2.0-0 \
    libnss3 \
    libxss1 \
    libgconf-2-4 \
    libfontconfig1 \
    libpangocairo-1.0-0 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxtst6 \
    libgdk-pixbuf2.0-0 \
    libgtk-3-0 \
    libgbm1 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Chrome 설치
RUN wget https://storage.googleapis.com/chrome-for-testing-public/123.0.6312.58/linux64/chrome-linux64.zip -P /tmp/ \
    && unzip /tmp/chrome-linux64.zip -d /opt/chrome/ \
    && ln -s /opt/chrome/chrome-linux64/chrome /usr/bin/google-chrome \
    && rm /tmp/chrome-linux64.zip

# ChromeDriver 설치
RUN wget https://storage.googleapis.com/chrome-for-testing-public/123.0.6312.58/linux64/chromedriver-linux64.zip -P /tmp/ \
    && unzip /tmp/chromedriver-linux64.zip -d /tmp/ \
    && mv /tmp/chromedriver-linux64/chromedriver /usr/local/bin/chromedriver \
    && chmod 755 /usr/local/bin/chromedriver \
    && rm /tmp/chromedriver-linux64.zip


# 애플리케이션 JAR 파일을 이미지에 추가
ARG JAR_FILE=build/libs/ggame-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} ggame-user.jar

# 애플리케이션 시작 명령어
ENTRYPOINT ["java","-jar","/ggame-user.jar"]
