# Depliants Manager App

## Description

The **Depliants Manager App** is a web-based application designed to manage and catalog depliants collected during
events or fairs. The application provides features for users to:

- Register and log in.
- Add, update, view, and delete depliants.
- Access only their own depliants.

The backend is built with **Spring Boot** and the frontend is powered by **Thymeleaf** templates. Database migrations
are handled using **Liquibase**, and **PostgreSQL** is used as the database.

## Features

- **User Management**: Users can register, log in, and manage their own depliants.
- **Depliants Management**:
    - Add depliants with fields like description, event name, notes, and language.
    - Edit or delete existing depliants.
    - Paginated view of depliants.
- **Secure Access**: Each user has access only to their depliants.

## Technology Stack

- **Backend**: Spring Boot
- **Frontend**: Thymeleaf
- **Database**: PostgreSQL
- **Database Migration**: Liquibase

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- PostgreSQL 12 or later

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/org-belligolifoundation/depliants-manager-app.git
   cd depliants-manager-app
   ```

2. **Configure the Database**

    - Create a PostgreSQL database:
      ```sql
      CREATE DATABASE dmadb;
      ```

    - Update the database credentials in ```src/main/resources/application.properties```:
       ```properties
       spring.datasource.url=jdbc:postgresql://localhost:5432/dmadb
       spring.datasource.username=your_postgres_user
       spring.datasource.password=your_postgres_password
       ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
4. **Access the Application**
   Open your browser and navigate to: http://localhost:8080
   Register a new user or log in with an existing account.

## Setup with Docker

Run [docker compose file](src/main/docker/depliants-manager-app/docker-compose.yml) to run the application as Docker
container.