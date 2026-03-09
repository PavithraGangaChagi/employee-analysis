package com.company.employee.reader;

import com.company.employee.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeFileReader {
    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int SALARY = 3;
    private static final int MANAGER_ID = 4;
    private static final String CSV_DELIMITER = ",";

    /**
     * Reads employee data from a CSV file and returns a list of Employee objects.
     *
     * @param filePath the path to the CSV file containing employee data
     * @return a list of Employee objects
     * @throws IOException if an I/O error occurs while reading the file
     */
    public List<Employee> readEmployees(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filePath))) {
            bufferedReader.readLine();
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                employees.add(getEmployee(line));
            }
        }
        return employees;
    }

    /**
     * Parses a line of CSV data and creates an Employee object.
     *
     * @param line a line of CSV data representing an employee
     * @return an Employee object created from the CSV data
     */
    private static Employee getEmployee(String line) {
        String[] data = line.split(CSV_DELIMITER, -1);
        int id = Integer.parseInt(data[ID].trim());
        String firstName = data[FIRST_NAME].trim();
        String lastName = data[LAST_NAME].trim();
        double salary = Double.parseDouble(data[SALARY].trim());
        Integer managerId = null;
        if (data.length > MANAGER_ID && !data[MANAGER_ID].trim().isEmpty()) {
            managerId = Integer.parseInt(data[MANAGER_ID].trim());
        }
        return new Employee(id, firstName, lastName, salary, managerId);
    }
}
