# Spring Boot SAGA Orchestration with Kafka & Flowable

## Overview

This project demonstrates an asynchronous SAGA Orchestration application using Spring Boot, Kafka, and Flowable as the orchestration engine. It includes the following services:

1. **flowable-saga-orchestrator-engine**: An event-driven Spring Boot SAGA Orchestration Engine with Flowable that communicates with other relevant microservices through Kafka.
2. **order-service**: Manages orders.
3. **inventory-service**: Manages inventory.
4. **payment-service**: Manages payments.
5. **shipping-service**: Manages shipping.

The project includes a `docker-compose.yml` file at the root for running Kafka locally. It uses the following dependencies:

- Spring-Boot: 3.4.2
- Flowable: 7.0.0
- Maven for build purposes.
- H2 In-memory DB to persist SAGA Workflow states

## Prerequisites

- Java 11+
- Maven 3.6+
- Docker Desktop and Docker Compose

## Setup Instructions

### Step 1: Clone the Repository

```sh
git clone https://github.com/Pronoy-Anirudhya/spring-flowable-event-driven-saga-example.git
```

### Step 2: Start Kafka using Docker Compose

Open Docker Desktop and go to the terminal. You can just navigate to the project's root directory where **docker-compose.yml** exists and run the following command.

```sh
docker-compose up -d
```

### Step 3: Build and Run Services

#### Build all services
Navigate to each project directory and build the services using Maven.

```sh
cd flowable-saga-orchestrator-engine
mvn clean install

cd order-service
mvn clean install

cd inventory-service
mvn clean install

cd payment-service
mvn clean install

cd shipping-service
mvn clean install
```

#### Run All Services
After building all the services, run them sequentially.

```sh
cd flowable-saga-orchestrator-engine
mvn spring-boot:run

cd .order-service
mvn spring-boot:run

cd inventory-service
mvn spring-boot:run

cd payment-service
mvn spring-boot:run

cd shipping-service
mvn spring-boot:run
```

### Step 4: Verify

If all the services along with Kafka are running successfully, run the following command in ```CMD``` to initiate an example Create Order SAGA.

```sh
curl -X POST "http://localhost:8080/orders/start?orderId=12345"
```

If the services are up and running, you should get the following response: ```Order Saga Started: <SAGA_PROCESS_GUID>```

### N.B. Docker Compose Configuration
The ```docker-compose.yml``` file in the root directory is used to set up Kafka. Make sure Docker and Docker Compose are installed and running before executing the following command:

```sh
docker-compose up -d
```
