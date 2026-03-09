package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.SalaryViolation;
import com.company.employee.model.ViolationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryAnalyzerTest {

    private EmployeeHierarchyService employeeHierarchyService;
    private SalaryAnalyzer salaryAnalyzer;

    @BeforeEach
    void setUp() {
        employeeHierarchyService = new EmployeeHierarchyService();
        salaryAnalyzer = new SalaryAnalyzer();
    }

    @Test
    void shouldReportManagerSalaryTooLow() {
        Employee manager = new Employee(1, "John", "Smith", 50000, null);
        Employee subordinate1 = new Employee(2, "Alice", "Johnson", 50000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 50000, 1);

        List<Employee> employees = List.of(manager, subordinate1, subordinate2);

        Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);
        Map<Integer, List<Employee>> subordinatesByManagerId =
                employeeHierarchyService.buildSubordinatesByManagerIdMap(employees);

        List<SalaryViolation> violations = salaryAnalyzer.analyze(subordinatesByManagerId, employeeById);

        assertEquals(1, violations.size());
        assertEquals(manager, violations.getFirst().manager());
        assertEquals(ViolationType.TOO_LOW, violations.getFirst().violationType());
        assertEquals(10000.0, violations.getFirst().amount(), 0.001);
    }

    @Test
    void shouldReportManagerSalaryTooHigh() {
        Employee manager = new Employee(1, "John", "Smith", 90000, null);
        Employee subordinate1 = new Employee(2, "Alice", "Johnson", 50000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 50000, 1);

        List<Employee> employees = List.of(manager, subordinate1, subordinate2);

        Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);
        Map<Integer, List<Employee>> subordinatesByManagerId =
                employeeHierarchyService.buildSubordinatesByManagerIdMap(employees);

        List<SalaryViolation> violations = salaryAnalyzer.analyze(subordinatesByManagerId, employeeById);

        assertEquals(1, violations.size());
        assertEquals(manager, violations.getFirst().manager());
        assertEquals(ViolationType.TOO_HIGH, violations.getFirst().violationType());
        assertEquals(15000.0, violations.getFirst().amount(), 0.001);
    }

    @Test
    void shouldNotReportViolationWhenManagerSalaryIsWithinRange() {
        Employee manager = new Employee(1, "John", "Smith", 65000, null);
        Employee subordinate1 = new Employee(2, "Alice", "Johnson", 50000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 50000, 1);

        List<Employee> employees = List.of(manager, subordinate1, subordinate2);

        Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);
        Map<Integer, List<Employee>> subordinatesByManagerId =
                employeeHierarchyService.buildSubordinatesByManagerIdMap(employees);

        List<SalaryViolation> violations = salaryAnalyzer.analyze(subordinatesByManagerId, employeeById);

        assertEquals(0, violations.size());
    }
}