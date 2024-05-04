# Distributed Systems for Algorithm Processing

## Project Overview

This project explores the capabilities of distributed architecture to solve basic programming algorithms, focusing specifically on the implementation of a microservices architecture to efficiently process and sort large datasets using MergeSort. This README details the system architecture, workflow, deployment strategies, and monitoring setup comprehensively.

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture Design](#architecture-design)
    - [System Components](#system-components)
    - [Infrastructure](#infrastructure)
- [Workflow](#workflow)
- [Deployment](#deployment)
    - [Docker and Kubernetes](#docker-and-kubernetes)
- [Monitoring and Tracing](#monitoring-and-tracing)
- [Security](#security)
- [Installation Guide](#installation-guide)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Architecture Design

### System Components

- **Coordinator Service**: Manages the division of the array into segments and orchestrates the sending and receiving of data to and from worker services.
- **Worker Service**: Responsible for processing the segments of the array it receives and sending back the sorted segments.

### Infrastructure


- **Kafka**: Handles the decoupling of data production by the coordinator from consumption by worker services.
- **Redis**: Stores the intermediate and final sorted arrays.

![Architecture Diagram](path_to_your_architecture_diagram_image)

## Workflow

1. **Request Handling**: Users send sort requests through the API Gateway, which authenticates and routes the requests to the Coordinator Service.
2. **Data Processing**:
    - The Coordinator divides the array and sends segments to Kafka.
    - Worker services pull segments, sort them, and publish the sorted segments back to Kafka.
3. **Data Aggregation**:
    - The Coordinator consumes the sorted segments from Kafka, combines them, and stores the result in Redis.
4. **User Response**: Users can query the final sorted results via the API Gateway.

![Workflow Diagram](path_to_your_workflow_diagram_image)

## Deployment

### Docker and Kubernetes

Explains the use of Docker and Kubernetes to containerize and orchestrate the deployment of services, ensuring scalability and reliability.

![Docker and Kubernetes Deployment Diagram](path_to_your_deployment_diagram_image)

## Monitoring and Tracing

Integration of Prometheus, Grafana, and Zipkin to provide monitoring and tracing capabilities for the system.

## Security

Details on the integration of Keycloak for securing the API Gateway and services.

## Installation Guide

Steps to set up the project environment, including installations of Docker, Kubernetes, and necessary configurations for all services.

## Usage

Instructions on how to send requests to the system and retrieve outputs, including example API calls and expected responses.

## Contributing

Guidelines for contributing to the project, including coding standards, pull request and review processes.

## License

Information about the project's license and usage rights.

