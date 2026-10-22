# QA Automation Assignment

UI and API test automation for two public demo applications:

- **UI:** [SauceDemo](https://www.saucedemo.com/), with Selenide
- **API:** [Restful Booker](https://restful-booker.herokuapp.com/), with REST Assured

The tests run locally with Maven or inside Docker with nothing else installed, and they produce an Allure report.

## Prerequisites

**To run locally:**
- JDK 17 or newer
- Maven 3.9 or newer
- Google Chrome (the matching ChromeDriver is downloaded automatically)

**To run in Docker:**
- Docker only.

Both ways need internet access, because the tests call the public demo sites.

## Local execution

| Run | Command |
|---|---|
| UI tests | `mvn clean test -Dtestng.suite=testng-ui.xml` |
| API tests | `mvn clean test -Dtestng.suite=testng-api.xml` |
| All tests | `mvn clean test` |

> **Linux, macOS, WSL or Git Bash on Windows:** run the commands exactly as shown.
>
> **Windows PowerShell:** put `-D` arguments in quotes, for example `mvn clean test "-Dtestng.suite=testng-ui.xml"`. PowerShell otherwise splits the argument at the dot.

**From IntelliJ IDEA:** right-click `testng.xml`, `testng-ui.xml` or `testng-api.xml` in `src/test/resources` and select **Run**. A single test class or method can be run with the green arrow next to it.

## Docker execution

Run these from the project root.

**Build the image:**

```bash
docker build -t qa-automation-tests .
```

**Run the tests:**

| Run | Command |
|---|---|
| All tests | `docker run --rm qa-automation-tests` |
| UI tests | `docker run --rm qa-automation-tests -Dtestng.suite=testng-ui.xml` |
| API tests | `docker run --rm qa-automation-tests -Dtestng.suite=testng-api.xml` |

**Keep the results** (Surefire reports, Allure results, Selenide screenshots) by mounting the `target` folder:

```bash
docker run --rm -v "$(pwd)/target:/app/target" qa-automation-tests
```

## Allure report

**Locally:**

```bash
mvn clean test
mvn allure:serve
```

**From Docker results:**

```bash
docker run --rm -v "$(pwd)/target:/app/target" qa-automation-tests
docker run --rm -v "$(pwd)/target:/app/target" --entrypoint mvn qa-automation-tests allure:report
python3 -m http.server 8080 --directory target/site/allure-maven-plugin
```

Then open http://localhost:8080. 

---

## Configuration

| File | Purpose |
|---|---|
| `config.properties` | SauceDemo credentials and the Restful Booker base URL |
| `selenide.properties` | Base URL, browser, headless mode, window size, timeout, output folders |
| `allure.properties` | Writes Allure results to `target/allure-results` |
| `simplelogger.properties` | Shows only warnings and errors in the console |
| `testng*.xml` | Which tests run, and in parallel (2 threads, by class) |

## Test approach

### Locators
All UI locators use SauceDemo's `data-test` attributes (`[data-test='username']`, `[data-test='inventory-item-name']`, ...).
- **They exist only for automation**, so they don't change when the page is restyled, unlike CSS classes or long XPath expressions.
- **They're present on every element the tests need.** Ids are missing from most of them, so one rule fits the whole project.
- **They're private to their page object**, so a changed locator is fixed in one place.

### Synchronization
There are no `sleep` calls and no hand-written waits. Selenide's `should…` checks retry until the condition holds or the timeout (6 s) runs out. Each action waits for its own visible result before the test continues:
- **After login:** the URL and the page title.
- **After sorting:** the sort label shows the selected option. It's re-rendered together with the list, so the list is ready when the label is.
- **After adding to the cart:** the cart badge shows the new count, before the cart is opened.
- **Before reading lists:** at least one item must be present, so an empty list can never pass as "sorted".

### Code structure
- **UI:** the Page Object pattern. Pages expose actions (`loginAs`, `sortBy`, `addToCart`) and checks (`shouldBeOpened`, `shouldContainOnly`), and return the page the user lands on, so tests read as one chain of steps.
- **API:** `BookingClient` holds the endpoints and a shared `RequestSpecification` (base URI, JSON headers, Allure logging) and returns raw responses. The test decides what to expect. `Booking` is a Java record used for both the request body and the response, so created and retrieved data are compared field by field.
- **Configuration** lives in one shared class and in properties files, not in the tests.
- **Test isolation:** each UI test gets a fresh browser, and each API test creates its own booking.

### Assertions
- **Hard assertions for steps of a flow:**
- **Soft assertions for independent checks on one result:**
- **Assertion messages name the field or include the actual value**

### Assumptions and limitations
- **The tests run against shared public demo servers.** Downtime or slowness there can fail a run for reasons outside this project.
- **Created bookings are not deleted.** Restful Booker's `DELETE` needs an auth token, and the public server resets its data regularly.
- **Restful Booker answers `POST /booking` with 200, not 201.** The test checks the actual documented behaviour.
- **The Docker image installs the latest stable Chrome**, so it isn't fixed to one exact version. Java, Maven and all libraries are fixed.
- **The Docker image is x86-64 only**, because Google publishes Chrome for Linux only for that architecture.
- **Allure trend history isn't kept between Docker runs**, because each container starts fresh.
