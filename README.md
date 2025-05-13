## Abstract:

A company that our team is working for is struggling with managing Human Resources-related functions. The company could benefit from having an application that would allow the various department managers and employees to manage and access and update their records. For example, a mobile app that allows an employee to review their own personal information and payment history, as well as allows a manager of that company to add or remove employees to or from their departments while updating the company's database would be ideal. Currently, the only thing that our company has that is close to this is a MySQL enterprise database. This database currently contains employee information, which ensures that the information is organized.

Unfortunately, a database is not enough, as it does not offer protection or efficiency. There is currently no user role based authentication needed when viewing the database tables, which exposes the company to data breaches and unauthorized access. Manually retrieving and updating information straight from the database is also an inefficient way to view and manage the data.

Our application, Tamales HR, will solve the problems that come with just having a database. Our team, The Tamales, will create an enterprise HR mobile application that can be used to allow employees to see and interact with the database data. We will implement user role based authentication for the application to ensure protection using Keycloak. The application will also give the company a streamlined way to read information about employees. This means keeping track of their general information such as their employee numbers, names, birth dates, hire dates, titles, departments, as well as their salaries. Moreover, it should allow managers to manage their employees by allowing them to view all the employees that work in the manager’s department, search for employees, add employees to their department, and remove employees from their department. We will also use GitHub and Jenkins to integrate and track software updates made by the team.

## Project Architecture Diagram:

<img width="637" alt="Screenshot 2025-05-12 at 6 19 01 PM" src="https://github.com/user-attachments/assets/27174b5e-f95b-4543-a100-aa93bc3b06b2" />

## Android App Class Diagram:

<img width="252" alt="Screenshot 2025-05-12 at 6 20 08 PM" src="https://github.com/user-attachments/assets/8448e85c-ad4f-4b11-8559-34edf992c152" />

## Main Controller Class Structure:

<img width="663" alt="Screenshot 2025-05-12 at 6 20 34 PM" src="https://github.com/user-attachments/assets/0e168b5a-9da6-4cca-9b14-da7689d056b8" />

## Employees DB Diagram:

<img width="743" alt="Screenshot 2025-05-12 at 6 21 00 PM" src="https://github.com/user-attachments/assets/e4a25b7a-9f66-4443-8c0d-e43867ec8ae5" />

## Keycloak User Provider Class Diagram:

<img width="339" alt="Screenshot 2025-05-12 at 6 21 37 PM" src="https://github.com/user-attachments/assets/6b6c6288-3e85-4be3-8c9d-8afb1453ce14" />
