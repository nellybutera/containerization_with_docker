FROM maven:3.9-eclipse-temurin-17

# CI=true triggers headless Chrome + --no-sandbox flags in BaseTest
ENV CI=true

# Install Google Chrome via direct .deb (avoids GPG key fetch flakiness)
RUN apt-get update && apt-get install -y wget && \
    wget -q -O /tmp/google-chrome.deb \
        https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt-get install -y /tmp/google-chrome.deb && \
    rm /tmp/google-chrome.deb && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Cache dependency layer separately so it is not re-downloaded on every code change
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src/ ./src/
COPY testng.xml .

CMD ["mvn", "test"]
