package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.ReportingLineViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportingLineAnalyzerTest {

    private EmployeeHierarchyService employeeHierarchyService;
    private ReportingLineAnalyzer reportingLineAnalyzer;

    @BeforeEach
    void setUp() {
        employeeHierarchyService = new EmployeeHierarchyService();
        reportingLineAnalyzer = new ReportingLineAnalyzer();
    }

    @Test
    void shouldReportEmployeeWhenReportingLineIsTooLong() {
        Employee ceo = new Employee(1, "John", "Smith", 100000, null);
        Employee manager1 = new Employee(2, "Manager", "One", 90000, 1);
        Employee manager2 = new Employee(3, "Manager", "Two", 80000, 2);
        Employee manager3 = new Employee(4, "Manager", "Three", 70000, 3);
        Employee manager4 = new Employee(5, "Manager", "Four", 60000, 4);
        Employee manager5 = new Employee(6, "Manager", "Five", 50000, 5);
        Employee employee = new Employee(7, "Alice", "Johnson", 40000, 6);

        List<Employee> employees = List.of(ceo, manager1, manager2, manager3, manager4, manager5, employee);

        Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);

        List<ReportingLineViolation> violations = reportingLineAnalyzer.analyze(employees, employeeById);

        assertEquals(1, violations.size());
        assertEquals(employee, violations.getFirst().employee());
        assertEquals(1, violations.getFirst().excessManagers());
    }

    @Test
    void shouldNotReportEmployeeWhenReportingLineIsWithinLimit() {
        Employee ceo = new Employee(1, "John", "Smith", 100000, null);
        Employee manager1 = new Employee(2, "Manager", "One", 90000, 1);
        Employee manager2 = new Employee(3, "Manager", "Two", 80000, 2);
        Employee manager3 = new Employee(4, "Manager", "Three", 70000, 3);
        Employee manager4 = new Employee(5, "Manager", "Four", 60000, 4);
        Employee employee = new Employee(6, "Alice", "Johnson", 40000, 5);

        List<Employee> employees = List.of(ceo, manager1, manager2, manager3, manager4, employee);

        Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);

        List<ReportingLineViolation> violations = reportingLineAnalyzer.analyze(employees, employeeById);

        assertEquals(0, violations.size());
    }
}