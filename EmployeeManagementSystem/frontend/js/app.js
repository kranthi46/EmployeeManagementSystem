// =====================================================
// JAVA SERVER URL
// =====================================================

const API_URL =
    "http://localhost:8080/api/employees";


// =====================================================
// GLOBAL EMPLOYEE ARRAY
// =====================================================

let employees = [];
let editingEmployeeId = null;


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadEmployees();

    }
);


// =====================================================
// LOAD EMPLOYEES FROM JAVA SERVER
// =====================================================

async function loadEmployees() {

    try {

        const response =
            await fetch(API_URL);

        if (!response.ok) {

            throw new Error(
                "Unable to load employees"
            );

        }

        employees =
            await response.json();

        updateDashboard();

        displayEmployees();

    } catch (error) {

        console.error(error);

        showServerError();

    }
}


// =====================================================
// SHOW SERVER ERROR
// =====================================================

function showServerError() {

    document.getElementById(
        "recentEmployees"
    ).innerHTML = `

        <p class="no-data">

            ⚠️ Unable to connect to Java server.

            <br><br>

            Make sure EmployeeServer.java
            is running on port 8080.

        </p>

    `;

}


// =====================================================
// PAGE NAVIGATION
// =====================================================

function showPage(page) {

    const dashboard =
        document.getElementById(
            "dashboardPage"
        );

    const employeesPage =
        document.getElementById(
            "employeesPage"
        );

    const addPage =
        document.getElementById(
            "addEmployeePage"
        );


    dashboard.classList.add(
        "hidden"
    );

    employeesPage.classList.add(
        "hidden"
    );

    addPage.classList.add(
        "hidden"
    );


    document
        .getElementById("dashboardNav")
        .classList.remove("active");

    document
        .getElementById("employeesNav")
        .classList.remove("active");

    document
        .getElementById("addNav")
        .classList.remove("active");


    if (page === "dashboard") {

        dashboard.classList.remove(
            "hidden"
        );

        document
            .getElementById(
                "dashboardNav"
            )
            .classList.add("active");

        updateDashboard();

    }


    else if (page === "employees") {

        employeesPage.classList.remove(
            "hidden"
        );

        document
            .getElementById(
                "employeesNav"
            )
            .classList.add("active");

        displayEmployees();

    }


    else if (page === "addEmployee") {

        addPage.classList.remove(
            "hidden"
        );

        document
            .getElementById(
                "addNav"
            )
            .classList.add("active");

    }

}


// =====================================================
// DASHBOARD
// =====================================================

function updateDashboard() {

    const total =
        employees.length;


    const totalSalary =
        employees.reduce(
            function (
                sum,
                employee
            ) {

                return sum +
                    Number(
                        employee.salary
                    );

            },
            0
        );


    const averageSalary =
        total > 0
            ? totalSalary / total
            : 0;


    const highestSalary =
        total > 0
            ? Math.max(
                ...employees.map(
                    function (employee) {

                        return Number(
                            employee.salary
                        );

                    }
                )
            )
            : 0;


    document.getElementById(
        "totalEmployees"
    ).textContent =
        total;


    document.getElementById(
        "totalSalary"
    ).textContent =
        formatCurrency(
            totalSalary
        );


    document.getElementById(
        "averageSalary"
    ).textContent =
        formatCurrency(
            Math.round(
                averageSalary
            )
        );


    document.getElementById(
        "highestSalary"
    ).textContent =
        formatCurrency(
            highestSalary
        );


    displayRecentEmployees();

    displayDepartments();

}


// =====================================================
// RECENT EMPLOYEES
// =====================================================

function displayRecentEmployees() {

    const container =
        document.getElementById(
            "recentEmployees"
        );


    if (employees.length === 0) {

        container.innerHTML = `

            <p class="no-data">
                No employees available.
            </p>

        `;

        return;
    }


    const recent =
        [...employees]
            .reverse()
            .slice(0, 5);


    container.innerHTML =
        recent.map(
            function (employee) {

                return `

                    <div class="recent-item">

                        <div class="avatar">

                            ${employee.name
                                .charAt(0)
                                .toUpperCase()}

                        </div>

                        <div class="recent-info">

                            <strong>
                                ${employee.name}
                            </strong>

                            <span>
                                ${employee.department}
                            </span>

                        </div>

                        <span class="recent-salary">

                            ${formatCurrency(
                                employee.salary
                            )}

                        </span>

                    </div>

                `;

            }
        ).join("");

}


// =====================================================
// DEPARTMENTS
// =====================================================

function displayDepartments() {

    const container =
        document.getElementById(
            "departments"
        );


    if (employees.length === 0) {

        container.innerHTML = `

            <p class="no-data">
                No departments available.
            </p>

        `;

        return;
    }


    const departmentMap = {};


    employees.forEach(
        function (employee) {

            const department =
                employee.department;


            if (
                departmentMap[
                    department
                ]
            ) {

                departmentMap[
                    department
                ]++;

            } else {

                departmentMap[
                    department
                ] = 1;

            }

        }
    );


    container.innerHTML =
        Object.entries(
            departmentMap
        ).map(
            function ([
                department,
                count
            ]) {

                return `

                    <div class="department-item">

                        <span>
                            ${department}
                        </span>

                        <span class="count-badge">
                            ${count}
                        </span>

                    </div>

                `;

            }
        ).join("");

}


// =====================================================
// DISPLAY EMPLOYEES
// =====================================================

function displayEmployees(employeeList = employees) {

    const tableBody =
        document.getElementById("employeeTableBody");

    tableBody.innerHTML = "";

    employeeList.forEach(function (employee) {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>#${employee.id}</td>

            <td>
                <div class="employee-info">

                    <div class="avatar">
                        ${employee.name
                            .charAt(0)
                            .toUpperCase()}
                    </div>

                    <span>
                        ${employee.name}
                    </span>

                </div>
            </td>

            <td>
                <span class="department">
                    ${employee.department}
                </span>
            </td>

            <td>
                ${employee.type}
            </td>

            <td>
                ₹${Number(employee.salary)
                    .toLocaleString("en-IN")}
            </td>

            <td>

                <button
                    class="edit-btn"
                    onclick="editEmployee(${employee.id})"
                >
                    Edit
                </button>

                <button
                    class="delete-btn"
                    onclick="deleteEmployee(${employee.id})"
                >
                    Delete
                </button>

            </td>
        `;

        tableBody.appendChild(row);
    });


    // Update employee count

    const employeeCount =
        document.getElementById(
            "employeeCount"
        );

    if (employeeCount) {

        employeeCount.textContent =
            `Showing ${employeeList.length} employees`;
    }
}
// =====================================================
// SEARCH
// =====================================================

function searchEmployees() {

    const searchText =
        document.getElementById("searchInput")
            .value
            .toLowerCase()
            .trim();

    const filteredEmployees =
        employees.filter(function (employee) {

            return (
                String(employee.id)
                    .includes(searchText) ||

                employee.name
                    .toLowerCase()
                    .includes(searchText) ||

                employee.department
                    .toLowerCase()
                    .includes(searchText) ||

                employee.type
                    .toLowerCase()
                    .includes(searchText)
            );
        });

    displayEmployees(filteredEmployees);
}

// =====================================================
// ADD EMPLOYEE
// =====================================================

// =====================================================
// ADD / UPDATE EMPLOYEE
// =====================================================

async function addEmployee(event) {

    event.preventDefault();


    const id =
        Number(
            document.getElementById(
                "employeeId"
            ).value
        );


    const name =
        document.getElementById(
            "employeeName"
        ).value.trim();


    const department =
        document.getElementById(
            "employeeDepartment"
        ).value;


    const role =
        document.getElementById(
            "employeeRole"
        ).value;


    const salary =
        Number(
            document.getElementById(
                "employeeSalary"
            ).value
        );


    const employee = {

        id: id,

        name: name,

        department: department,

        salary: salary,

        type: role

    };


    try {

        // =================================================
        // UPDATE
        // =================================================

        if (editingEmployeeId !== null) {

            const response =
                await fetch(
                    `${API_URL}/${editingEmployeeId}`,
                    {

                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                employee
                            )

                    }
                );


            if (!response.ok) {

                let message =
                    "Unable to update employee.";


                try {

                    const error =
                        await response.json();

                    message =
                        error.message ||
                        message;

                } catch (e) {
                }


                throw new Error(
                    message
                );
            }


            const updatedEmployee =
                await response.json();


            // Update local array

            employees =
                employees.map(
                    function (item) {

                        if (
                            item.id ===
                            editingEmployeeId
                        ) {

                            return updatedEmployee;
                        }

                        return item;
                    }
                );


            alert(
                "Employee updated successfully!"
            );


            cancelEdit();


            updateDashboard();

            showPage("employees");


            return;
        }


        // =================================================
        // ADD
        // =================================================

        const response =
            await fetch(
                API_URL,
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(
                            employee
                        )

                }
            );


        if (!response.ok) {

            let message =
                "Unable to add employee.";


            try {

                const error =
                    await response.json();

                message =
                    error.message ||
                    message;

            } catch (e) {
            }


            throw new Error(
                message
            );
        }


        const newEmployee =
            await response.json();


        employees.push(
            newEmployee
        );


        alert(
            "Employee added successfully!"
        );


        document
            .getElementById(
                "employeeForm"
            )
            .reset();


        updateDashboard();

        showPage("employees");


    } catch (error) {

        showFormError(
            error.message
        );

    }
}

// =====================================================
// CANCEL EDIT
// =====================================================

function cancelEdit() {

    editingEmployeeId = null;


    document
        .getElementById(
            "employeeForm"
        )
        .reset();


    document.getElementById(
        "formTitle"
    ).textContent =
        "Add Employee";


    document.getElementById(
        "submitEmployeeBtn"
    ).textContent =
        "Add Employee";


    document.getElementById(
        "cancelEditBtn"
    ).classList.add(
        "hidden"
    );


    document.getElementById(
        "formError"
    ).classList.add(
        "hidden"
    );
}

// =====================================================
// DELETE EMPLOYEE
// =====================================================

async function deleteEmployee(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this employee?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                `${API_URL}/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to delete employee."
            );

        }


        employees =
            employees.filter(
                function (employee) {

                    return employee.id !== id;

                }
            );


        displayEmployees();

        updateDashboard();


        alert(
            "Employee deleted successfully!"
        );


    } catch (error) {

        alert(
            error.message
        );

    }

}


// =====================================================
// FORM ERROR
// =====================================================

function showFormError(
    message
) {

    const error =
        document.getElementById(
            "formError"
        );


    error.textContent =
        "⚠️ " + message;


    error.classList.remove(
        "hidden"
    );

}


// =====================================================
// CURRENCY FORMAT
// =====================================================

function formatCurrency(
    amount
) {

    return (
        "₹" +
        Number(amount)
            .toLocaleString(
                "en-IN"
            )
    );

}
// =====================================================
// EDIT EMPLOYEE
// =====================================================

function editEmployee(id) {

    const employee =
        employees.find(
            function (employee) {
                return employee.id === id;
            }
        );

    if (!employee) {

        alert("Employee not found.");

        return;
    }


    // Store the employee ID
    editingEmployeeId = id;


    // Fill the form
    document.getElementById(
        "employeeId"
    ).value = employee.id;


    document.getElementById(
        "employeeName"
    ).value = employee.name;


    document.getElementById(
        "employeeDepartment"
    ).value = employee.department;


    document.getElementById(
        "employeeSalary"
    ).value = employee.salary;


    // Role is only displayed.
    // EmployeeService currently updates
    // name, department and salary.
    if (employee.type) {

        document.getElementById(
            "employeeRole"
        ).value = employee.type;

    }


    // Change title
    document.getElementById(
        "formTitle"
    ).textContent = "Edit Employee";


    // Change button
    document.getElementById(
        "submitEmployeeBtn"
    ).textContent = "Update Employee";


    // Show cancel button
    document.getElementById(
        "cancelEditBtn"
    ).classList.remove("hidden");


    // Open form
    showPage("addEmployee");
}