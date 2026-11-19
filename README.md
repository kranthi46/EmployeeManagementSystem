# Employee Management System

## 📌 Project Overview

The **Employee Management System** is a Java-based application designed to manage employee information and perform employee-related operations through a web-based frontend.

The project uses Java for backend development and HTML, CSS, and JavaScript for the frontend. It uses Java's built-in HTTP server to handle client requests and communicate between the frontend and backend without requiring Apache Tomcat.

## 🚀 Technologies Used

### Backend
- Java 21
- Java HTTP Server
- Object-Oriented Programming (OOP)
- Exception Handling

### Frontend
- HTML5
- CSS3
- JavaScript

### Development Tools
- Eclipse IDE
- Git
- GitHub

## 📂 Project Structure

```text
EmployeeManagementSystem/
│
├── src/
│   ├── exception/
│   │   ├── DuplicateEmployeeException.java
│   │   ├── EmployeeNotFoundException.java
│   │   └── InvalidSalaryException.java
│   │
│   ├── main/
│   │   └── EmployeeManagementSystem.java
│   │
│   ├── model/
│   │   ├── Developer.java
│   │   ├── Employee.java
│   │   ├── Manager.java
│   │   └── Tester.java
│   │
│   ├── server/
│   │   └── EmployeeServer.java
│   │
│   └── service/
│       └── EmployeeService.java
│
└── frontend/
    ├── css/
    ├── js/
    └── index.html
```

## ✨ Key Concepts Implemented

- Object-Oriented Programming
- Inheritance and Polymorphism
- Encapsulation
- Exception Handling
- Custom Exception Classes
- Separation of Model, Service, and Server Layers
- Frontend and Backend Communication
- Java HTTP Server

## ⚙️ How to Run the Project

### Prerequisites

- Java JDK 21 or later
- Eclipse IDE
- Web browser

### Steps

1. Clone the repository:

   ```bash
   git clone YOUR_GITHUB_REPOSITORY_URL
   ```

2. Open Eclipse IDE.

3. Import the project:
   - Select **File → Import**.
   - Choose **Existing Projects into Workspace**.
   - Select the project folder.

4. Open `EmployeeServer.java`.

5. Right-click and select:

   **Run As → Java Application**

6. Open your browser and navigate to:

   ```text
   http://localhost:8080
   ```

7. The Employee Management System frontend should open.

## 🧠 Exception Handling

The project includes custom exception classes to handle specific situations:

- `DuplicateEmployeeException` – Handles duplicate employee records.
- `EmployeeNotFoundException` – Handles employee records that cannot be found.
- `InvalidSalaryException` – Handles invalid salary values.

## 🎯 Project Objective

The main objective of this project is to understand Java backend development, object-oriented programming principles, exception handling, and communication between a Java server and a web-based frontend.

## 👨‍💻 Author

**Potnuru Sai Kranthi Kumar**

