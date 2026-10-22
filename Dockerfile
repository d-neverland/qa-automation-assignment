FROM maven:3.9.11-eclipse-temurin-17

RUN apt-get update \
    && apt-get install -y --no-install-recommends wget \
    && wget -q -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y --no-install-recommends /tmp/chrome.deb \
    && rm /tmp/chrome.deb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B test-compile

ENTRYPOINT ["mvn", "-B", "test", "-Dchromeoptions.args=--no-sandbox,--disable-dev-shm-usage"]