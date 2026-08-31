package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import exception.DuplicateEmployeeException;
import exception.EmployeeNotFoundException;
import exception.InvalidSalaryException;

import model.Developer;
import model.Employee;
import model.Manager;
import model.Tester;

import service.EmployeeService;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EmployeeServer {

    private static final int PORT = 8080;

    private static final EmployeeService employeeService =
            new EmployeeService();

    public static void main(String[] args) throws IOException {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        // REST API
        server.createContext(
                "/api/employees",
                EmployeeServer::handleEmployees
        );

        // Frontend
        server.createContext(
                "/",
                EmployeeServer::handleFrontend
        );

        server.setExecutor(null);

        System.out.println(
                "===================================="
        );

        System.out.println(
                "Employee Management Server Started"
        );

        System.out.println(
                "Frontend: http://localhost:" + PORT
        );

        System.out.println(
                "API:      http://localhost:" +
                        PORT +
                        "/api/employees"
        );

        System.out.println(
                "===================================="
        );

        server.start();
    }


    // =====================================================
    // EMPLOYEE API
    // =====================================================

    private static void handleEmployees(
            HttpExchange exchange) {

        try {

            addCorsHeaders(exchange);

            String method =
                    exchange.getRequestMethod();

            String path =
                    exchange.getRequestURI()
                            .getPath();

            System.out.println(
                    method + " " + path
            );


            // ---------------------------------------------
            // OPTIONS
            // ---------------------------------------------

            if (method.equalsIgnoreCase("OPTIONS")) {

            	exchange.sendResponseHeaders(204, -1);
            	exchange.close();

                return;
            }


            // ---------------------------------------------
            // GET
            // ---------------------------------------------

            if (method.equalsIgnoreCase("GET")) {

                handleGet(
                        exchange,
                        path
                );

                return;
            }


            // ---------------------------------------------
            // POST
            // ---------------------------------------------

            if (method.equalsIgnoreCase("POST")) {

                handlePost(exchange);

                return;
            }


            // ---------------------------------------------
            // PUT
            // ---------------------------------------------

            if (method.equalsIgnoreCase("PUT")) {

                handlePut(
                        exchange,
                        path
                );

                return;
            }


            // ---------------------------------------------
            // DELETE
            // ---------------------------------------------

            if (method.equalsIgnoreCase("DELETE")) {

                handleDelete(
                        exchange,
                        path
                );

                return;
            }


            sendJson(
                    exchange,
                    405,
                    "{\"message\":\"Method not allowed\"}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            try {

                sendJson(
                        exchange,
                        500,
                        jsonError(
                                "Internal server error: "
                                        + e.getMessage()
                        )
                );

            } catch (IOException ignored) {
            }
        }
    }


    // =====================================================
    // GET
    // =====================================================

    private static void handleGet(
            HttpExchange exchange,
            String path)
            throws IOException {

        /*
         * /api/employees
         */

        if (path.equals("/api/employees")) {

            Collection<Employee> employees =
                    employeeService.getAllEmployees();

            String json =
                    employeesToJson(employees);

            sendJson(
                    exchange,
                    200,
                    json
            );

            return;
        }


        /*
         * /api/employees/{id}
         */

        String prefix =
                "/api/employees/";

        if (path.startsWith(prefix)) {

            String idText =
                    path.substring(
                            prefix.length()
                    );

            try {

                int id =
                        Integer.parseInt(idText);

                Employee employee =
                        employeeService
                                .getEmployeeById(id);

                sendJson(
                        exchange,
                        200,
                        employeeToJson(employee)
                );

            } catch (NumberFormatException e) {

                sendJson(
                        exchange,
                        400,
                        jsonError(
                                "Invalid employee ID."
                        )
                );

            } catch (
                    EmployeeNotFoundException e) {

                sendJson(
                        exchange,
                        404,
                        jsonError(
                                e.getMessage()
                        )
                );
            }

            return;
        }


        sendJson(
                exchange,
                404,
                jsonError(
                        "Endpoint not found."
                )
        );
    }


    // =====================================================
    // POST
    // =====================================================

    private static void handlePost(
            HttpExchange exchange)
            throws IOException {

        try {

            String requestBody =
                    readRequestBody(exchange);

            System.out.println(
                    "Request body: "
                            + requestBody
            );


            Map<String, String> data =
                    parseJson(requestBody);


            int id =
                    Integer.parseInt(
                            data.get("id")
                    );

            String name =
                    data.get("name");

            String department =
                    data.get("department");

            double salary =
                    Double.parseDouble(
                            data.get("salary")
                    );

            String type =
                    data.get("type");


            Employee employee =
                    createEmployee(
                            id,
                            name,
                            department,
                            salary,
                            type
                    );


            employeeService.addEmployee(
                    employee
            );


            sendJson(
                    exchange,
                    201,
                    employeeToJson(employee)
            );

        } catch (
                DuplicateEmployeeException |
                InvalidSalaryException e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            e.getMessage()
                    )
            );

        } catch (NumberFormatException e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            "Invalid ID or salary."
                    )
            );

        } catch (Exception e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            e.getMessage()
                    )
            );
        }
    }


    // =====================================================
    // PUT
    // =====================================================

    private static void handlePut(
            HttpExchange exchange,
            String path)
            throws IOException {

        String prefix =
                "/api/employees/";

        if (!path.startsWith(prefix)) {

            sendJson(
                    exchange,
                    404,
                    jsonError(
                            "Employee endpoint not found."
                    )
            );

            return;
        }


        String idText =
                path.substring(
                        prefix.length()
                );


        try {

            int id =
                    Integer.parseInt(idText);


            String requestBody =
                    readRequestBody(exchange);


            Map<String, String> data =
                    parseJson(requestBody);


            String name =
                    data.get("name");

            String department =
                    data.get("department");

            double salary =
                    Double.parseDouble(
                            data.get("salary")
                    );


            employeeService.updateEmployee(
                    id,
                    name,
                    department,
                    salary
            );


            Employee updated =
                    employeeService
                            .getEmployeeById(id);


            sendJson(
                    exchange,
                    200,
                    employeeToJson(updated)
            );


        } catch (NumberFormatException e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            "Invalid employee ID or salary."
                    )
            );

        } catch (
                EmployeeNotFoundException |
                InvalidSalaryException e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            e.getMessage()
                    )
            );
        }
    }


    // =====================================================
    // DELETE
    // =====================================================

    private static void handleDelete(
            HttpExchange exchange,
            String path)
            throws IOException {

        String prefix =
                "/api/employees/";

        if (!path.startsWith(prefix)) {

            sendJson(
                    exchange,
                    404,
                    jsonError(
                            "Employee endpoint not found."
                    )
            );

            return;
        }


        String idText =
                path.substring(
                        prefix.length()
                );


        try {

            int id =
                    Integer.parseInt(idText);


            employeeService.deleteEmployee(
                    id
            );


            sendJson(
                    exchange,
                    200,
                    "{\"message\":\"Employee deleted successfully.\"}"
            );


        } catch (NumberFormatException e) {

            sendJson(
                    exchange,
                    400,
                    jsonError(
                            "Invalid employee ID."
                    )
            );

        } catch (
                EmployeeNotFoundException e) {

            sendJson(
                    exchange,
                    404,
                    jsonError(
                            e.getMessage()
                    )
            );
        }
    }


    // =====================================================
    // CREATE EMPLOYEE OBJECT
    // =====================================================

    private static Employee createEmployee(
            int id,
            String name,
            String department,
            double salary,
            String type) {

        if (type == null ||
                type.trim().isEmpty()) {

            type = "Employee";
        }


        switch (type.toLowerCase()) {

            case "developer":

                return new Developer(
                        id,
                        name,
                        department,
                        salary
                );


            case "manager":

                return new Manager(
                        id,
                        name,
                        department,
                        salary
                );


            case "tester":

                return new Tester(
                        id,
                        name,
                        department,
                        salary
                );


            default:

                return new Employee(
                        id,
                        name,
                        department,
                        salary
                );
        }
    }


    // =====================================================
    // FRONTEND
    // =====================================================

    private static void handleFrontend(
            HttpExchange exchange) {

        try {

            String path =
                    exchange.getRequestURI()
                            .getPath();


            if (path.equals("/")) {

                path = "/index.html";
            }


            Path frontendRoot =
                    Paths.get(
                            "frontend"
                    ).toAbsolutePath()
                            .normalize();


            Path file =
                    frontendRoot
                            .resolve(
                                    path.substring(1)
                            )
                            .normalize();


            /*
             * Security:
             * Don't allow access outside
             * frontend folder.
             */

            if (!file.startsWith(
                    frontendRoot)) {

                sendText(
                        exchange,
                        403,
                        "Forbidden"
                );

                return;
            }


            if (!Files.exists(file) ||
                    Files.isDirectory(file)) {

                sendText(
                        exchange,
                        404,
                        "File not found"
                );

                return;
            }


            byte[] data =
                    Files.readAllBytes(file);


            String contentType =
                    getContentType(
                            file
                    );


            Headers headers =
                    exchange.getResponseHeaders();

            headers.set(
                    "Content-Type",
                    contentType
            );


            exchange.sendResponseHeaders(
                    200,
                    data.length
            );


            OutputStream output =
                    exchange.getResponseBody();

            output.write(data);

            output.close();


        } catch (Exception e) {

            e.printStackTrace();

            try {

                sendText(
                        exchange,
                        500,
                        "Unable to load frontend."
                );

            } catch (IOException ignored) {
            }
        }
    }


    // =====================================================
    // CONTENT TYPE
    // =====================================================

    private static String getContentType(
            Path file) {

        String fileName =
                file.getFileName()
                        .toString()
                        .toLowerCase();


        if (fileName.endsWith(".html")) {

            return "text/html; charset=UTF-8";
        }


        if (fileName.endsWith(".css")) {

            return "text/css; charset=UTF-8";
        }


        if (fileName.endsWith(".js")) {

            return "application/javascript; charset=UTF-8";
        }


        if (fileName.endsWith(".json")) {

            return "application/json; charset=UTF-8";
        }


        if (fileName.endsWith(".png")) {

            return "image/png";
        }


        if (fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg")) {

            return "image/jpeg";
        }


        if (fileName.endsWith(".svg")) {

            return "image/svg+xml";
        }


        return "application/octet-stream";
    }


    // =====================================================
    // JSON - EMPLOYEE
    // =====================================================

    private static String employeeToJson(
            Employee employee) {

        String type =
                employee
                        .getClass()
                        .getSimpleName();


        return "{"
                + "\"id\":"
                + employee.getId()
                + ","
                + "\"name\":\""
                + escapeJson(
                        employee.getName()
                )
                + "\","
                + "\"department\":\""
                + escapeJson(
                        employee.getDepartment()
                )
                + "\","
                + "\"salary\":"
                + employee.getSalary()
                + ","
                + "\"type\":\""
                + type
                + "\""
                + "}";
    }


    // =====================================================
    // JSON - EMPLOYEE COLLECTION
    // =====================================================

    private static String employeesToJson(
            Collection<Employee> employees) {

        StringBuilder json =
                new StringBuilder();

        json.append("[");


        boolean first = true;


        for (Employee employee :
                employees) {

            if (!first) {

                json.append(",");
            }

            json.append(
                    employeeToJson(employee)
            );

            first = false;
        }


        json.append("]");


        return json.toString();
    }


    // =====================================================
    // SIMPLE JSON PARSER
    // =====================================================

    private static Map<String, String>
    parseJson(String json) {

        Map<String, String> data =
                new HashMap<>();


        json =
                json.trim();


        if (json.startsWith("{")) {

            json =
                    json.substring(1);
        }


        if (json.endsWith("}")) {

            json =
                    json.substring(
                            0,
                            json.length() - 1
                    );
        }


        String[] pairs =
                json.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"
                );


        for (String pair : pairs) {

            String[] keyValue =
                    pair.split(
                            ":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)",
                            2
                    );


            if (keyValue.length != 2) {

                continue;
            }


            String key =
                    cleanJsonValue(
                            keyValue[0]
                    );


            String value =
                    cleanJsonValue(
                            keyValue[1]
                    );


            data.put(
                    key,
                    value
            );
        }


        return data;
    }


    // =====================================================
    // CLEAN JSON VALUE
    // =====================================================

    private static String cleanJsonValue(
            String value) {

        value =
                value.trim();


        if (value.startsWith("\"") &&
                value.endsWith("\"")) {

            value =
                    value.substring(
                            1,
                            value.length() - 1
                    );
        }


        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }


    // =====================================================
    // READ REQUEST BODY
    // =====================================================

    private static String readRequestBody(
            HttpExchange exchange)
            throws IOException {

        InputStream input =
                exchange.getRequestBody();


        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();


        byte[] data =
                new byte[1024];


        int bytesRead;


        while (
                (bytesRead =
                        input.read(data))
                        != -1
        ) {

            buffer.write(
                    data,
                    0,
                    bytesRead
            );
        }


        return buffer
                .toString(
                        StandardCharsets.UTF_8
                );
    }


    // =====================================================
    // CORS
    // =====================================================

    private static void addCorsHeaders(
            HttpExchange exchange) {

        Headers headers =
                exchange
                        .getResponseHeaders();


        headers.set(
                "Access-Control-Allow-Origin",
                "*"
        );


        headers.set(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );


        headers.set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }


    // =====================================================
    // SEND JSON
    // =====================================================

    private static void sendJson(
            HttpExchange exchange,
            int statusCode,
            String json)
            throws IOException {

        Headers headers =
                exchange
                        .getResponseHeaders();


        headers.set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );


        byte[] response =
                json.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.sendResponseHeaders(
                statusCode,
                response.length
        );


        OutputStream output =
                exchange.getResponseBody();


        output.write(response);

        output.close();
    }


    // =====================================================
    // SEND TEXT
    // =====================================================

    private static void sendText(
            HttpExchange exchange,
            int statusCode,
            String text)
            throws IOException {

        Headers headers =
                exchange
                        .getResponseHeaders();


        headers.set(
                "Content-Type",
                "text/plain; charset=UTF-8"
        );


        byte[] response =
                text.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.sendResponseHeaders(
                statusCode,
                response.length
        );


        OutputStream output =
                exchange.getResponseBody();


        output.write(response);

        output.close();
    }


    // =====================================================
    // ERROR JSON
    // =====================================================

    private static String jsonError(
            String message) {

        return "{"
                + "\"message\":\""
                + escapeJson(message)
                + "\""
                + "}";
    }


    // =====================================================
    // ESCAPE JSON
    // =====================================================

    private static String escapeJson(
            String text) {

        if (text == null) {

            return "";
        }


        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}