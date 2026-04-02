package com.company.employee.reader;

import com.company.employee.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileReader {
    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int SALARY = 3;
    private static final int MANAGER_ID = 4;
    private static final int REQUIRED_COLUMNS = 4;
    private static final CSVFormat EMPLOYEE_CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreSurroundingSpaces(true)
            .build();

    /**
     * Reads employee data from a CSV file and returns a list of Employee objects.
     *
     * @param filePath the path to the CSV file containing employee data
     * @return a list of Employee objects
     * @throws IOException if an I/O error occurs while reading the file
     */
    public List<Employee> readEmployees(String filePath) throws IOException {
        validateFilePath(filePath);

        Path path = Path.of(filePath);

        try (BufferedReader bufferedReader = Files.newBufferedReader(path);
             CSVParser parser = EMPLOYEE_CSV_FORMAT.parse(bufferedReader)) {
            for (CSVRecord record : parser) {
                try {
                    employees.add(parseEmployee(record));
                } catch (MalformedEmployeeRowException ex) {
                    // Optionally, log this exception or collect errors
                    throw ex;
                }
            }
            return employees;
        }
    }

    /**
     * Parses a line of CSV data and creates an Employee object.
     *
     * @param line a line of CSV data representing an employee
     * @return an Employee object created from the CSV data
     */
    private static Employee parseEmployee(CSVRecord record) {
        ensureMinimumColumns(record);

        try {
            int id = Integer.parseInt(record.get(ID).trim());
            String firstName = record.get(FIRST_NAME).trim();
            String lastName = record.get(LAST_NAME).trim();
            double salary = Double.parseDouble(record.get(SALARY).trim());
            Integer managerId = null;

            if (record.size() > MANAGER_ID) {
                String managerIdValue = record.get(MANAGER_ID).trim();
                if (!managerIdValue.isEmpty()) {
                    managerId = Integer.parseInt(managerIdValue);
                }
            }

            return new Employee(id, firstName, lastName, salary, managerId);
        } catch (NumberFormatException ex) {
            throw new MalformedEmployeeRowException(
                    "Malformed row at line " + record.getRecordNumber()
                            + ": numeric fields could not be parsed. Row=" + record,
                    ex
            );
        }
    }

    private static void ensureMinimumColumns(CSVRecord record) {
        if (record.size() < REQUIRED_COLUMNS) {
            throw new MalformedEmployeeRowException(
                    "Malformed row at line " + record.getRecordNumber()
                            + ": expected at least " + REQUIRED_COLUMNS + " columns but found "
                            + record.size() + ". Row=" + record
            );
        }
    }

    private static void validateFilePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath must not be null or blank");
        }
    }
}
