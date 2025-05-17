# Tamales HR - Enterprise Human Resources Management App

## Abstract

Our client is facing significant challenges in managing Human Resources (HR) operations. Although they maintain a well-structured MySQL enterprise database containing employee information, this setup lacks the necessary tools for secure and efficient data management. Currently, there's no role-based authentication in place, leaving sensitive data vulnerable to unauthorized access. Additionally, manually retrieving and updating records directly from the database is both time-consuming and error-prone.

To address these issues, our team—**The Tamales**—has developed **Tamales HR**, an enterprise mobile application designed to streamline HR management processes. The application empowers employees to securely access their personal information and payment history. Simultaneously, department managers gain robust tools to manage their teams, such as:

- Viewing employees within their department
- Adding or removing employees from their department
- Searching for employee records
- Updating company database records seamlessly

To ensure data security and proper access control, we've integrated **Keycloak** for user role-based authentication. This guarantees that only authorized personnel can perform specific actions based on their role.

Furthermore, we adopted industry best practices for development and collaboration, using **GitHub** for version control and **Jenkins** for continuous integration, ensuring smooth deployment and efficient team coordination.

## Features

### ✅ Secure User Authentication
- **Role-based access control** implemented via **Keycloak**.
- Differentiates between **Employees** and **Managers**, granting access to features based on their roles.
- Protects sensitive data from unauthorized access.

### 📱 Mobile HR Portal (Android Application)
- Employees can:
  - View personal information (name, birth date, hire date, etc.).
  - Review salary history and payment records.
- Managers can:
  - View all employees in their department.
  - Add employees to their department.
  - Remove employees from their department.
  - Search for specific employees.

### 🗄️ Enterprise-Grade Backend Integration
- Seamless integration with the existing **MySQL employee database**.
- Real-time data synchronization ensures that updates made via the app reflect instantly in the company’s database.

### 🧩 Custom Keycloak User Storage Provider
- Custom-built **Keycloak SPI (Service Provider Interface)** to federate users directly from the existing employee database.
- Maps employees to Keycloak users dynamically without duplicating data.
- Supports user authentication based on employee credentials stored in the database.

### 🔄 Continuous Integration & Version Control
- **GitHub** for collaborative development and source code management.
- **Jenkins CI/CD pipeline** automates builds, testing, and deployment, ensuring high-quality and up-to-date application versions.

### 📊 Scalable and Modular Architecture
- Follows a clean separation of concerns between **frontend**, **backend services**, and **authentication provider**.
- Designed for scalability, making it easy to extend functionalities (e.g., leave requests, benefits management) in future versions.

## Getting Started

This section will guide you through setting up **Tamales HR** on your local machine for development and testing purposes. Follow the steps below to get the backend services, Keycloak authentication, and Android application up and running.

### Prerequisites

Make sure you have the following installed:

- **Java 21** 
- **MySQL 8.x**
- **Docker & Docker Compose**
- **Android Studio (Giraffe or newer)**
- **Gradle 8.x (or use Gradle Wrapper)**
- **Git**
- **Jenkins (optional for CI)**

### Project Structure
tamales-hr/
├── backend/ # Spring Boot backend service
├── keycloak-provider/ # Custom Keycloak SPI for MySQL user federation
├── mobile-app/ # Android mobile application (Kotlin)
├── database/ # SQL scripts and sample data
├── jenkins/ # Jenkins pipeline configurations
├── README.md # This file
└── build.gradle / build.gradle.kts

### Setup Instructions

#### 1. Clone the Repository

```bash
git clone https://github.com/your-org/tamales-hr.git
cd tamales-hr
```

#### 2. Setup MySQL Database
Import the provided schema and sample data
```bash
mysql -u root -p < {{YOUR_PATH}}/employees.sql
```

#### 3. Run Keycloak Server with MySQL Integration
Go to the path where keycloak server is
```bash
cd {{YOUR_PATH}}/keycloak/spi/UserServiceProviderInterface
```
Build all the gradle dependencies
```bash
./gradlew clean quarkusBuild --refresh-dependencies
```
Start the server with docker
```bash
docker build -f keycloak-debug.Dockerfile -t keycloak-debug .
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin keycloak-debug
```

### 4. Run Spring Boot Server API
Go to the path where backend server is
```bash
cd {{YOUR_PATH}}/backend/tamalesHr 
```
Run Spring Server
```bash
./gradlew bootRun
```

### 5. Open Android Studio and run the app