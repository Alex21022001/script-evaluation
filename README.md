# Script Evaluation

This project is a REST API wrapper built with Spring Boot that integrates with the GraalJS JavaScript interpreter. It allows you to evaluate arbitrary JavaScript code through RESTful endpoints, providing features like non-blocking execution, script monitoring, and more.

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

