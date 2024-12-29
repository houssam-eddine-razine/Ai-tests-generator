# Automated Test Generation Application

This project is an innovative application designed to automate the process of test generation. It takes a zipped folder as input, navigates through its structure to identify classes and methods, and generates automated test cases for these methods. The application leverages modern technologies such as Angular for the frontend, Spring Boot for the backend, and Docker for containerized deployment.

## Features

- **Zipped Folder Processing**:
  - Accepts a zipped folder as input.
  - Unzips and navigates through the folder to locate Java classes and their methods.
- **Automated Test Generation**:
  - Analyzes identified methods and generates unit test cases.
  - Supports common testing frameworks such as JUnit.
- **User Authentication**:
  - Secure login functionality with role-based access control.
  - Default credentials:
    - **Username**: `testUser`
    - **Password**: `password123`
- **Microservices Architecture**:
  - Decoupled services for managing file uploads, test generation, and authentication.
- **Responsive Design**:
  - Optimized user interface for different screen sizes and devices.
- **CI/CD Pipeline**:
  - Automated build, test, and deployment processes with Jenkins.
- **Code Quality Analysis**:
  - Integrated with SonarQube for code quality and security checks.
 
## Implementation Video




https://github.com/user-attachments/assets/56126ddc-0e04-44cf-a677-e94705573d14



## Project Architecture

Below is  representation of the project architecture:

- **Frontend** (Angular Application): Sends HTTP requests for authentication and test generation.
- **API Gateway**: Routes requests to the appropriate microservices (authentication, test generation).
- **Backend Microservices** (Spring Boot):
- **Auth-Service**: Handles user authentication and session management.
- **Test-Gen-Service**: Processes the zipped folder and generates test cases.
- **Database**: Stores user sessions, metadata, and generated test cases.
- **Docker**: Containerizes each service for deployment.
- **Jenkins** (CI/CD): Manages automated build and deployment pipelines.


### Key Components

- **Frontend**: Angular application providing the user interface for uploading files and managing test generation.
- **Backend Microservices**:
  - **Auth-Service**: Handles authentication and user management.
  - **File-Service**: Processes zipped folders and identifies classes and methods.
  - **Test-Gen-Service**: Generates test cases based on identified methods.
- **Database**: MySQL containerized using Docker.
- **CI/CD Tools**:
  - **Jenkins**: Automates build, test, and deployment pipelines.
  - **SonarQube**: Ensures code quality and detects vulnerabilities.

## Technologies Used

- **Frontend**: Angular 18 with Angular Material for UI components.
- **Backend**: Spring Boot for microservices.
- **Database**: MySQL.
- **Containerization**: Docker for deploying services.
- **CI/CD**: Jenkins.
- **Code Quality**: SonarQube.

## Prerequisites

Ensure you have the following installed:

- **Node.js**: v18 or higher.
- **Angular CLI**: v18 or higher.
- **Java**: JDK 17 or higher.
- **Maven**: Latest version.
- **Docker**: Latest version.
- **Git**: Latest version.

## Installation and Setup

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/test-gen-app.git
cd test-gen-app
```

### 2. Backend Setup

- Navigate to each microservice folder (`auth-service`, `file-service`, `test-gen-service`, etc.) and run:

```bash
mvn clean install
```

### 3. Frontend Setup

- Navigate to the `frontend` folder:

```bash
cd frontend
npm install
```

- Start the Angular development server:

```bash
ng serve
```

### 4. Docker Setup

- Build and run the Docker containers:

```bash
docker-compose up --build
```

- Verify the containers are running:

```bash
docker ps
```

### 5. Jenkins and SonarQube

- Access Jenkins at `http://localhost:8081` and configure pipelines.
- Access SonarQube at `http://localhost:9000` for code quality analysis.

## Usage

### Uploading and Analyzing Files

1. Login using the provided credentials.
2. Upload a zipped folder containing Java source files.
3. The application will:
   - Unzip the folder.
   - Analyze the structure to locate classes and methods.
   - Generate unit test cases for the identified methods.

### CI/CD Pipeline

1. Push code to the GitHub repository.
2. Jenkins automatically triggers build and deployment pipelines.
3. Verify build status and deployments.

## Contribution Guidelines

1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit changes and push to the branch:
   ```bash
   git push origin feature-name
   ```
4. Create a pull request for review.


