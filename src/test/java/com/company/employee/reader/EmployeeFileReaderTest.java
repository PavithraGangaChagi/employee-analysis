package com.company.employee.reader;

import com.company.employee.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeFileReaderTest {

    private final EmployeeFileReader employeeFileReader = new EmployeeFileReader();

    @TempDir
    Path tempDir;

    @Test
    void givenQuotedCommaInName_whenReadEmployees_thenParsesRowCorrectly() throws IOException {
        Path csv = writeCsv(
                "id,first_name,last_name,salary,manager_id",
                "1,\"John, Jr.\",Doe,1000,",
                "2,Alice,Smith,2000,1"
        );

        List<Employee> employees = employeeFileReader.readEmployees(csv.toString());

        assertEquals(2, employees.size());
        assertEquals("John, Jr.", employees.get(0).firstName());
        assertEquals("Doe", employees.get(0).lastName());
        assertNull(employees.get(0).managerId());
    }

    @Test
    void givenMissingRequiredColumns_whenReadEmployees_thenThrowsMalformedRowException() throws IOException {
        Path csv = writeCsv(
                "id,first_name,last_name,salary,manager_id",
                "1,John"
        );

        MalformedEmployeeRowException exception = assertThrows(
                MalformedEmployeeRowException.class,
                () -> employeeFileReader.readEmployees(csv.toString())
        );

        assertTrue(exception.getMessage().contains("expected at least 4 columns"));
        assertTrue(exception.getMessage().contains("line 1"));
    }

    @Test
    void givenInvalidNumericValue_whenReadEmployees_thenThrowsMalformedRowException() throws IOException {
        Path csv = writeCsv(
                "id,first_name,last_name,salary,manager_id",
                "1,John,Doe,not-a-number,"
        );

        MalformedEmployeeRowException exception = assertThrows(
                MalformedEmployeeRowException.class,
                () -> employeeFileReader.readEmployees(csv.toString())
        );

        assertTrue(exception.getMessage().contains("numeric fields could not be parsed"));
        assertTrue(exception.getMessage().contains("line 1"));
    }

    @Test
    void givenBlankFilePath_whenReadEmployees_thenThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeFileReader.readEmployees("  ")
        );

        assertEquals("filePath must not be null or blank", exception.getMessage());
    }

    @Test
    void givenNullFilePath_whenReadEmployees_thenThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeFileReader.readEmployees(null)
        );

        assertEquals("filePath must not be null or blank", exception.getMessage());
    }

    @Test
    void givenOnlyHeader_whenReadEmployees_thenReturnsEmptyList() throws IOException {
        Path csv = writeCsv("id,first_name,last_name,salary,manager_id");

        List<Employee> employees = employeeFileReader.readEmployees(csv.toString());

        assertTrue(employees.isEmpty());
    }

    @Test
    void givenWhitespaceAroundValues_whenReadEmployees_thenTrimsParsedFields() throws IOException {
        Path csv = writeCsv(
                "id,first_name,last_name,salary,manager_id",
                " 1 ,  John  , Doe  ,  1500.50  ,  "
        );

        List<Employee> employees = employeeFileReader.readEmployees(csv.toString());

        assertEquals(1, employees.size());
        Employee employee = employees.getFirst();
        assertEquals(1, employee.id());
        assertEquals("John", employee.firstName());
        assertEquals("Doe", employee.lastName());
        assertEquals(1500.50, employee.salary());
        assertNull(employee.managerId());
    }

    @Test
    void givenInvalidManagerId_whenReadEmployees_thenThrowsMalformedRowException() throws IOException {
        Path csv = writeCsv(
                "id,first_name,last_name,salary,manager_id",
                "1,John,Doe,1000,abc"
        );

        MalformedEmployeeRowException exception = assertThrows(
                MalformedEmployeeRowException.class,
                () -> employeeFileReader.readEmployees(csv.toString())
        );

        assertTrue(exception.getMessage().contains("numeric fields could not be parsed"));
        assertTrue(exception.getMessage().contains("line 1"));
    }

    @Test
    void givenNonExistentFile_whenReadEmployees_thenThrowsIOException() {
        Path missingPath = tempDir.resolve("missing.csv");

        IOException exception = assertThrows(
                IOException.class,
                () -> employeeFileReader.readEmployees(missingPath.toString())
        );

        assertNotNull(exception);
    }

    private Path writeCsv(String... lines) throws IOException {
        long fileCount = Files.list(tempDir).count();
        Path file = tempDir.resolve("employees-" + fileCount + ".csv");
        Files.write(file, List.of(lines));
        return file;
    }
}
