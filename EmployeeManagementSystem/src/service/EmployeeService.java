package service;

import java.util.*;
import java.util.stream.Collectors;

import model.Employee;
import exception.EmployeeNotFoundException;
import exception.DuplicateEmployeeException;
import exception.InvalidSalaryException;

public class EmployeeService {

    // MAP
    // Employee ID -> Employee Object

    private Map<Integer, Employee> employees =
            new HashMap<>();


    // ==============================================
    // ADD EMPLOYEE
    // ==============================================

    public void addEmployee(Employee employee)
            throws DuplicateEmployeeException,
                   InvalidSalaryException {

        if (employees.containsKey(employee.getId())) {

            throw new DuplicateEmployeeException(
                    "Employee ID already exists: "
                            + employee.getId()
            );
        }

        if (employee.getSalary() < 0) {

            throw new InvalidSalaryException(
                    "Salary cannot be negative."
            );
        }

        employees.put(
                employee.getId(),
                employee
        );

        System.out.println(
                "Employee added successfully."
        );
    }


    // ==============================================
    // DISPLAY ALL EMPLOYEES
    // ==============================================

    public void displayAllEmployees()
            throws EmployeeNotFoundException {

        if (employees.isEmpty()) {

            throw new EmployeeNotFoundException(
                    "No employees available."
            );
        }

        employees.values()
                .forEach(System.out::println);
    }


    // ==============================================
    // SEARCH EMPLOYEE
    // ==============================================

    public Employee findEmployee(int id)
            throws EmployeeNotFoundException {

        Employee employee =
                employees.get(id);

        if (employee == null) {

            throw new EmployeeNotFoundException(
                    "Employee not found with ID: "
                            + id
            );
        }

        return employee;
    }


    // ==============================================
    // UPDATE EMPLOYEE
    // ==============================================

    public void updateEmployee(
            int id,
            String name,
            String department,
            double salary)
            throws EmployeeNotFoundException,
                   InvalidSalaryException {

        Employee employee =
                findEmployee(id);

        if (salary < 0) {

            throw new InvalidSalaryException(
                    "Salary cannot be negative."
            );
        }

        employee.setName(name);
        employee.setDepartment(department);
        employee.setSalary(salary);

        System.out.println(
                "Employee updated successfully."
        );
    }


    // ==============================================
    // DELETE EMPLOYEE
    // ==============================================

    public void deleteEmployee(int id)
            throws EmployeeNotFoundException {

        if (!employees.containsKey(id)) {

            throw new EmployeeNotFoundException(
                    "Employee not found with ID: "
                            + id
            );
        }

        employees.remove(id);

        System.out.println(
                "Employee deleted successfully."
        );
    }


    // ==============================================
    // SORT BY NAME
    // ==============================================

    public void sortByName()
            throws EmployeeNotFoundException {

        checkEmpty();

        employees.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Employee::getName
                        )
                )
                .forEach(System.out::println);
    }


    // ==============================================
    // SORT BY SALARY
    // ==============================================

    public void sortBySalary()
            throws EmployeeNotFoundException {

        checkEmpty();

        employees.values()
                .stream()
                .sorted(
                        Comparator.comparingDouble(
                                Employee::getSalary
                        )
                )
                .forEach(System.out::println);
    }


    // ==============================================
    // HIGHEST SALARY
    // ==============================================

    public void highestSalary()
            throws EmployeeNotFoundException {

        checkEmpty();

        Employee employee =
                employees.values()
                        .stream()
                        .max(
                                Comparator.comparingDouble(
                                        Employee::getSalary
                                )
                        )
                        .orElseThrow(
                                () ->
                                new EmployeeNotFoundException(
                                        "No employees found."
                                )
                        );

        System.out.println(
                "Highest Paid Employee:"
        );

        System.out.println(employee);
    }


    // ==============================================
    // LOWEST SALARY
    // ==============================================

    public void lowestSalary()
            throws EmployeeNotFoundException {

        checkEmpty();

        Employee employee =
                employees.values()
                        .stream()
                        .min(
                                Comparator.comparingDouble(
                                        Employee::getSalary
                                )
                        )
                        .orElseThrow(
                                () ->
                                new EmployeeNotFoundException(
                                        "No employees found."
                                )
                        );

        System.out.println(
                "Lowest Paid Employee:"
        );

        System.out.println(employee);
    }


    // ==============================================
    // AVERAGE SALARY
    // ==============================================

    public void averageSalary()
            throws EmployeeNotFoundException {

        checkEmpty();

        double average =
                employees.values()
                        .stream()
                        .mapToDouble(
                                Employee::getSalary
                        )
                        .average()
                        .orElse(0);

        System.out.println(
                "Average Salary: "
                        + average
        );
    }


    // ==============================================
    // TOTAL SALARY
    // ==============================================

    public void totalSalary()
            throws EmployeeNotFoundException {

        checkEmpty();

        double total =
                employees.values()
                        .stream()
                        .map(
                                Employee::getSalary
                        )
                        .reduce(
                                0.0,
                                Double::sum
                        );

        System.out.println(
                "Total Salary: "
                        + total
        );
    }


    // ==============================================
    // FIND BY DEPARTMENT
    // ==============================================

    public void findByDepartment(
            String department)
            throws EmployeeNotFoundException {

        List<Employee> result =
                employees.values()
                        .stream()
                        .filter(
                                e -> e.getDepartment()
                                        .equalsIgnoreCase(
                                                department
                                        )
                        )
                        .collect(
                                Collectors.toList()
                        );

        if (result.isEmpty()) {

            throw new EmployeeNotFoundException(
                    "No employees found in department: "
                            + department
            );
        }

        result.forEach(
                System.out::println
        );
    }


    // ==============================================
    // UNIQUE DEPARTMENTS - SET
    // ==============================================

    public void showDepartments()
            throws EmployeeNotFoundException {

        checkEmpty();

        Set<String> departments =
                employees.values()
                        .stream()
                        .map(
                                Employee::getDepartment
                        )
                        .collect(
                                Collectors.toSet()
                        );

        System.out.println(
                "Departments:"
        );

        departments.forEach(
                System.out::println
        );
    }


    // ==============================================
    // GROUP BY DEPARTMENT
    // ==============================================

    public void groupByDepartment()
            throws EmployeeNotFoundException {

        checkEmpty();

        Map<String, List<Employee>> result =
                employees.values()
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Employee::getDepartment
                                )
                        );

        result.forEach(
                (department, employeeList) -> {

                    System.out.println(
                            "\nDepartment: "
                                    + department
                    );

                    employeeList.forEach(
                            System.out::println
                    );
                }
        );
    }


    // ==============================================
    // COUNT EMPLOYEES BY DEPARTMENT
    // ==============================================

    public void countByDepartment()
            throws EmployeeNotFoundException {

        checkEmpty();

        Map<String, Long> result =
                employees.values()
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Employee::getDepartment,
                                        Collectors.counting()
                                )
                        );

        result.forEach(
                (department, count) ->
                        System.out.println(
                                department
                                        + " = "
                                        + count
                        )
        );
    }


    // ==============================================
    // TOP 3 HIGHEST PAID
    // ==============================================

    public void topThreeEmployees()
            throws EmployeeNotFoundException {

        checkEmpty();

        employees.values()
                .stream()
                .sorted(
                        Comparator
                                .comparingDouble(
                                        Employee::getSalary
                                )
                                .reversed()
                )
                .limit(3)
                .forEach(
                        System.out::println
                );
    }


    // ==============================================
    // EMPLOYEES WITH SALARY > GIVEN AMOUNT
    // ==============================================

    public void salaryGreaterThan(
            double salary)
            throws EmployeeNotFoundException {

        checkEmpty();

        List<Employee> result =
                employees.values()
                        .stream()
                        .filter(
                                e -> e.getSalary()
                                        > salary
                        )
                        .collect(
                                Collectors.toList()
                        );

        if (result.isEmpty()) {

            System.out.println(
                    "No employees found."
            );

            return;
        }

        result.forEach(
                System.out::println
        );
    }


    // ==============================================
    // HELPER METHOD
    // ==============================================

    private void checkEmpty()
            throws EmployeeNotFoundException {

        if (employees.isEmpty()) {

            throw new EmployeeNotFoundException(
                    "No employees available."
            );
        }
    }
    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }
    public Employee getEmployeeById(int id)
            throws EmployeeNotFoundException {

        return findEmployee(id);
    }
}