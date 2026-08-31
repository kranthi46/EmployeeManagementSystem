package main;

import java.util.Scanner;

import model.Employee;
import model.Developer;
import model.Manager;
import model.Tester;

import service.EmployeeService;

import exception.EmployeeNotFoundException;
import exception.DuplicateEmployeeException;
import exception.InvalidSalaryException;

public class EmployeeManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeService service =
                new EmployeeService();

        int choice;

        do {

            displayMenu();

            System.out.print(
                    "Enter your choice: "
            );

            choice = readInt(sc);

            try {

                switch (choice) {

                    case 1:
                        addEmployee(sc, service);
                        break;

                    case 2:
                        service.displayAllEmployees();
                        break;

                    case 3:
                        searchEmployee(sc, service);
                        break;

                    case 4:
                        updateEmployee(sc, service);
                        break;

                    case 5:
                        deleteEmployee(sc, service);
                        break;

                    case 6:
                        service.sortByName();
                        break;

                    case 7:
                        service.sortBySalary();
                        break;

                    case 8:
                        service.highestSalary();
                        break;

                    case 9:
                        service.lowestSalary();
                        break;

                    case 10:
                        service.averageSalary();
                        break;

                    case 11:
                        service.totalSalary();
                        break;

                    case 12:
                        findDepartment(sc, service);
                        break;

                    case 13:
                        service.showDepartments();
                        break;

                    case 14:
                        service.groupByDepartment();
                        break;

                    case 15:
                        service.countByDepartment();
                        break;

                    case 16:
                        service.topThreeEmployees();
                        break;

                    case 17:
                        salaryGreaterThan(sc, service);
                        break;

                    case 18:
                        System.out.println(
                                "Thank you for using "
                                + "Employee Management System!"
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (
                    EmployeeNotFoundException |
                    DuplicateEmployeeException |
                    InvalidSalaryException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }

            System.out.println();

        } while (choice != 18);

        sc.close();
    }


    // =================================================
    // MENU
    // =================================================

    private static void displayMenu() {

        System.out.println(
                "\n========================================"
        );

        System.out.println(
                "       EMPLOYEE MANAGEMENT SYSTEM"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "1.  Add Employee"
        );

        System.out.println(
                "2.  View All Employees"
        );

        System.out.println(
                "3.  Search Employee"
        );

        System.out.println(
                "4.  Update Employee"
        );

        System.out.println(
                "5.  Delete Employee"
        );

        System.out.println(
                "6.  Sort by Name"
        );

        System.out.println(
                "7.  Sort by Salary"
        );

        System.out.println(
                "8.  Highest Salary"
        );

        System.out.println(
                "9.  Lowest Salary"
        );

        System.out.println(
                "10. Average Salary"
        );

        System.out.println(
                "11. Total Salary"
        );

        System.out.println(
                "12. Find by Department"
        );

        System.out.println(
                "13. Show Unique Departments"
        );

        System.out.println(
                "14. Group by Department"
        );

        System.out.println(
                "15. Count by Department"
        );

        System.out.println(
                "16. Top 3 Highest Paid"
        );

        System.out.println(
                "17. Salary Greater Than"
        );

        System.out.println(
                "18. Exit"
        );

        System.out.println(
                "========================================"
        );
    }


    // =================================================
    // ADD EMPLOYEE
    // =================================================

    private static void addEmployee(
            Scanner sc,
            EmployeeService service)
            throws DuplicateEmployeeException,
                   InvalidSalaryException {

        System.out.print(
                "Enter Employee ID: "
        );

        int id = readInt(sc);

        System.out.print(
                "Enter Employee Name: "
        );

        String name = sc.nextLine();

        System.out.print(
                "Enter Department: "
        );

        String department = sc.nextLine();

        System.out.print(
                "Enter Salary: "
        );

        double salary = readDouble(sc);

        System.out.println(
                "\nSelect Employee Type:"
        );

        System.out.println(
                "1. Employee"
        );

        System.out.println(
                "2. Developer"
        );

        System.out.println(
                "3. Manager"
        );

        System.out.println(
                "4. Tester"
        );

        System.out.print(
                "Enter type: "
        );

        int type = readInt(sc);

        Employee employee;

        switch (type) {

            case 2:
                employee =
                        new Developer(
                                id,
                                name,
                                department,
                                salary
                        );
                break;

            case 3:
                employee =
                        new Manager(
                                id,
                                name,
                                department,
                                salary
                        );
                break;

            case 4:
                employee =
                        new Tester(
                                id,
                                name,
                                department,
                                salary
                        );
                break;

            default:
                employee =
                        new Employee(
                                id,
                                name,
                                department,
                                salary
                        );
        }

        service.addEmployee(employee);
    }


    // =================================================
    // SEARCH
    // =================================================

    private static void searchEmployee(
            Scanner sc,
            EmployeeService service)
            throws EmployeeNotFoundException {

        System.out.print(
                "Enter Employee ID: "
        );

        int id = readInt(sc);

        Employee employee =
                service.findEmployee(id);

        System.out.println(employee);
    }


    // =================================================
    // UPDATE
    // =================================================

    private static void updateEmployee(
            Scanner sc,
            EmployeeService service)
            throws EmployeeNotFoundException,
                   InvalidSalaryException {

        System.out.print(
                "Enter Employee ID: "
        );

        int id = readInt(sc);

        System.out.print(
                "Enter New Name: "
        );

        String name = sc.nextLine();

        System.out.print(
                "Enter New Department: "
        );

        String department = sc.nextLine();

        System.out.print(
                "Enter New Salary: "
        );

        double salary = readDouble(sc);

        service.updateEmployee(
                id,
                name,
                department,
                salary
        );
    }


    // =================================================
    // DELETE
    // =================================================

    private static void deleteEmployee(
            Scanner sc,
            EmployeeService service)
            throws EmployeeNotFoundException {

        System.out.print(
                "Enter Employee ID: "
        );

        int id = readInt(sc);

        service.deleteEmployee(id);
    }


    // =================================================
    // FIND DEPARTMENT
    // =================================================

    private static void findDepartment(
            Scanner sc,
            EmployeeService service)
            throws EmployeeNotFoundException {

        System.out.print(
                "Enter Department: "
        );

        String department =
                sc.nextLine();

        service.findByDepartment(
                department
        );
    }


    // =================================================
    // SALARY FILTER
    // =================================================

    private static void salaryGreaterThan(
            Scanner sc,
            EmployeeService service)
            throws EmployeeNotFoundException {

        System.out.print(
                "Enter Salary: "
        );

        double salary =
                readDouble(sc);

        service.salaryGreaterThan(
                salary
        );
    }


    // =================================================
    // INTEGER INPUT
    // =================================================

    private static int readInt(
            Scanner sc) {

        while (true) {

            try {

                return Integer.parseInt(
                        sc.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.print(
                        "Please enter a valid integer: "
                );
            }
        }
    }


    // =================================================
    // DOUBLE INPUT
    // =================================================

    private static double readDouble(
            Scanner sc) {

        while (true) {

            try {

                return Double.parseDouble(
                        sc.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.print(
                        "Please enter a valid number: "
                );
            }
        }
    }
}