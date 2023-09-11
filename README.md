# Script Evaluation

This project is a REST API wrapper built with Spring Boot that integrates with the GraalJS JavaScript interpreter. It
allows you to evaluate arbitrary JavaScript code through RESTful endpoints, providing features like non-blocking
execution, script monitoring, and more.

## Features

- Evaluate arbitrary JavaScript code through RESTful API requests.
- Capture script output and errors during execution.
- View a list of scripts, their statuses, execution times, and other contextual information.
- Retrieve detailed information about a specific script, including its source code and console output/error.
- Terminate running or scheduled scripts by their ID to free up system resources.
- Remove inactive scripts (stopped, completed, failed) from the script list.

## Prerequisites

- Java Development Kit (JDK) 17
- Maven Wrapper (included in the project)

## Build and Run Instructions

1. Clone the repository:

```bash
git clone https://github.com/Alex21022001/script-evaluation.git
```

2. Go to the directory

```bash
cd script-evaluation
```

3. Build the project using Maven Wrapper

```bash
./mvnw clean install
```

4. Go to the Target directory

```bash
cd .\target
```

5. Run the app

```bash
java -jar script-evaluation-0.0.1-SNAPSHOT.jar
```

#### You also can run the app via Docker, take a look at the last section (Monitoring).

## How to Use

### Swagger UI

1. After running the application, open your web browser and navigate to: http://localhost:8080/swagger-ui.html
2. Use the Swagger UI interface to explore the available endpoints, test requests, and view responses.

### Postman

1. Download and install Postman.
2. Import the provided Postman collection: **_scripts-evaluation.postman_collection.json_**
3. You'll find a collection of API requests that you can use to interact with the application.

## Metrics and Monitoring

This application includes metrics and monitoring powered by Prometheus and Grafana. To access the metrics and custom
dashboards:

1. Ensure you have Docker installed.
2. Clone the repository:

```bash
git clone https://github.com/Alex21022001/script-evaluation.git
```

3. Go to the directory

```bash
cd script-evaluation
```

4. Build the project using Maven Wrapper

```bash
./mvnw clean install
```

5. Run docker-compose file

```bash
docker-compose up -d --build
```

6. Go to http://localhost:8080. 
Script evaluation will be at http://localhost:8080/app Prometheus will be available at http://localhost:8080/prometheus, and Grafana at http://localhost:8080/grafana (default login:
   admin/admin).

8. Import custom Grafana dashboards from the **dashboard** directory to visualize specific application metrics.










