# Tamales HR - Enterprise Human Resources Management App

Our client is facing significant challenges in managing Human Resources (HR) operations. Although they maintain a well-structured MySQL enterprise database containing employee information, this setup lacks the necessary tools for secure and efficient data management. Currently, there's no role-based authentication in place, leaving sensitive data vulnerable to unauthorized access. Additionally, manually retrieving and updating records directly from the database is both time-consuming and error-prone.

To address these issues, our team—**The Tamales**—has developed **Tamales HR**, an enterprise mobile application designed to streamline HR management processes. The application empowers employees to securely access their personal information and payment history. Simultaneously, department managers gain robust tools to manage their teams, such as:

- Viewing employees within their department
- Adding or removing employees from their department
- Searching for employee records
- Updating company database records seamlessly

To ensure data security and proper access control, we've integrated **Keycloak** for user role-based authentication. This guarantees that only authorized personnel can perform specific actions based on their role.

Furthermore, we adopted industry best practices for development and collaboration, using **GitHub** for version control and **Jenkins** for continuous integration, ensuring smooth deployment and efficient team coordination.


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
```
tamales-hr/
├── backend/ # Spring Boot backend service
├── keycloak/ # Custom Keycloak SPI for MySQL user federation
├── frontend/ # Android mobile application (Kotlin)
├── README.md # This file
```

### Setup Instructions

#### 1. Clone the Repository

```bash
git clone https://github.com/your-org/tamales-hr.git
cd tamales-hr
```

#### 2. Setup MySQL Database
Import the provided schema with sample data
```bash
mysql -u root -p < {{YOUR_PATH}}/employees.sql
```
Add employee_password table and populated with default values
```sql
CREATE TABLE employee_passwords (
    emp_no INT NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_employee_passwords_emp_no
        FOREIGN KEY (emp_no)
        REFERENCES employees(emp_no)
        ON DELETE CASCADE
);

INSERT INTO employee_passwords (emp_no, password)
SELECT emp_no, 'Password@123'
FROM employees
WHERE emp_no NOT IN (
  SELECT emp_no FROM employee_passwords
);

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

#### 4. Run Spring Boot Server API
Go to the path where backend server is
```bash
cd {{YOUR_PATH}}/backend/tamalesHr 
```
Run Spring Server
```bash
./gradlew bootRun
```

#### 5. Open Android Studio and run the app

https://github.com/user-attachments/assets/1fc9bf7c-3d66-4eea-9d23-c7f6dd282a7f

## Features

### ✅ Secure User Authentication
- **Role-based access control** implemented via **Keycloak**.
- Differentiates between **Employees** and **Managers**, granting access to features based on their roles.
- Protects sensitive data from unauthorized access.

#### Manager Authentication Demo
https://github.com/user-attachments/assets/18c9ec33-e705-4316-b6fc-8d0761b6c818

<img src="https://github.com/user-attachments/assets/773eea42-66b5-4f74-bd70-47fc3f6d750b" width=500 />

#### Employee Authentication Demo
https://github.com/user-attachments/assets/6923c5e3-cb89-442c-9895-ae79fa1b16a7

<img src="https://github.com/user-attachments/assets/c96b9355-4363-4c08-aeca-b286bf80d613" width=500 />

### 📱 Mob

ile HR Portal (Android Application)
- Employees can:
  - View personal information (name, birth date, hire date, etc.).
  - Review salary history and payment records.
    
https://github.com/user-attachments/assets/b7111442-3969-4113-bf1e-c9f0329d4bef

- Managers can:
  - View all employees in their department.
  - Add employees to their department.
  - Remove employees from their department.
  - Search for specific employees.
 
https://github.com/user-attachments/assets/5a2a73ea-992d-4b63-8737-bb59b24e3957

### 🧩 Custom Keycloak User Storage Provider
- Custom-built **Keycloak SPI (Service Provider Interface)** to federate users directly from the existing employee database.
- Maps employees to Keycloak users dynamically without duplicating data.
- Supports user authentication based on employee credentials stored in the database.

https://github.com/user-attachments/assets/b2d65ab4-f350-41ab-ac6b-b2f933b2cc21

### 📊 Scalable and Modular Architecture

#### Project Architecture Diagram:
<img width="500" alt="Screenshot 2025-05-12 at 6 19 01 PM" src="https://github.com/user-attachments/assets/27174b5e-f95b-4543-a100-aa93bc3b06b2" />

#### Android App Class Diagram:
<img width="500" alt="Screenshot 2025-05-12 at 6 20 08 PM" src="https://github.com/user-attachments/assets/8448e85c-ad4f-4b11-8559-34edf992c152" />

#### Main Controller Class Structure:
<img width="500" alt="Screenshot 2025-05-12 at 6 20 34 PM" src="https://github.com/user-attachments/assets/0e168b5a-9da6-4cca-9b14-da7689d056b8" />

#### Employees DB Diagram:
<img width="500" alt="Screenshot 2025-05-12 at 6 21 00 PM" src="https://github.com/user-attachments/assets/e4a25b7a-9f66-4443-8c0d-e43867ec8ae5" />

#### Keycloak User Provider Class Diagram:
<img width="500" alt="Screenshot 2025-05-12 at 6 21 37 PM" src="https://github.com/user-attachments/assets/6b6c6288-3e85-4be3-8c9d-8afb1453ce14" />

### 🧪 Testing
To ensure the reliability and robustness of Tamales HR, we implemented tests checking the functionality at all layers.

###### Backend: Tested individual service and repository layers using JUnit5 and Mockito.
<img width="400" alt="Screenshot 2025-05-17 at 12 09 22 AM" src="https://github.com/user-attachments/assets/045315b1-ef95-4433-9019-16e497f3d8bf" />


###### Keycloak: Validate user credentials, user creation , and removal.

<img width="400" alt="Screenshot 2025-05-17 at 12 08 40 AM" src="https://github.com/user-attachments/assets/c0a3e3f6-7cfa-43bf-b25e-2418a3abc8d0" />


###### Frontend (Android): ViewModels and business logic tested with JUnit , MockK, and Espresso usinf Robolectric to ensure correctness.

<img width="400" alt="Screenshot 2025-05-17 at 12 07 50 AM" src="https://github.com/user-attachments/assets/e2124b8a-b8be-415e-ba20-90a5502054a3" />


### 🔄 Continuous Integration & Version Control
- **GitHub** for collaborative development and source code management.
- **Jenkins CI/CD pipeline** automates builds, testing, and deployment, ensuring high-quality and up-to-date application versions.






