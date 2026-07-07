# Swag Labs Test Automation — Dockerized

Selenium + TestNG UI test suite for [saucedemo.com](https://www.saucedemo.com/), containerized with Docker and run automatically via GitHub Actions on every push/PR to `main`.

## Stack

- Java 17, Maven 3.9
- Selenium 4.18.1 + WebDriverManager (auto-manages the ChromeDriver binary)
- TestNG 7.10.1 (suite defined in `testng.xml`)
- Allure (test reporting, with automatic screenshot capture on failure)
- Page Object Model under `src/main/java/com/saucedemo/pages`

## Project structure

```
src/main/java/com/saucedemo/pages/   Page Objects (Login, Inventory, Cart, Checkout*, BasePage)
src/test/java/com/saucedemo/tests/   Test classes (LoginTest, CartTest, CheckoutTest)
src/test/java/com/saucedemo/base/    BaseTest — WebDriver lifecycle (setUp/tearDown per test method)
src/test/java/com/saucedemo/utils/   TestConfig (properties reader), CsvDataProvider
src/test/resources/testdata.properties   Static config: base URL, credentials, item names
src/test/resources/testdata/*.csv    Data-driven rows for validation tests
testng.xml                           Suite definition (Login / Cart / Checkout test groups)
Dockerfile                           Builds an image that installs Chrome and runs `mvn test`
.github/workflows/docker-test.yml    CI: builds the image and runs it on push/PR to main
```

## Running locally (no Docker)

Requires a local Chrome install; WebDriverManager downloads the matching driver automatically.

```bash
mvn test
```

Run headless locally the same way CI does:

```bash
mvn test -Dheadless=true
```

## Running with Docker

Requires Docker Desktop running.

```bash
docker build -t swag-labs-tests .
docker run --rm swag-labs-tests
```

The image sets `CI=true`, which `BaseTest` reads to launch Chrome with `--headless=new`.

## CI pipeline

`.github/workflows/docker-test.yml` triggers on every push/PR to `main`, builds the Docker image, and runs the container. A failing test suite fails the build (`mvn test` returns non-zero on any test failure).

## Test reports (Allure)

Failed tests automatically attach a screenshot to the Allure result. To view a report after a local run:

```bash
mvn test
allure serve allure-results
```

## Adding test data

Static values (URLs, valid credentials, happy-path form data) go in `src/test/resources/testdata.properties`.
Data-driven validation scenarios (invalid login combos, incomplete checkout forms) go in the CSV files under `src/test/resources/testdata/` — add a row, no Java changes needed.
