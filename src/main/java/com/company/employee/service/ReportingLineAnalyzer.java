package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.ReportingLineViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportingLineAnalyzer {

    private static final int MAX_MANAGERS_ALLOWED = 4;

    /**
     * Analyzes the reporting lines of employees to identify any violations where an employee has more than the allowed number of managers between them and the CEO.
     *
     * @param employees    the list of Employee objects to analyze
     * @param employeeById a map of employee IDs to Employee objects for quick lookup
     * @return a list of ReportingLineViolation objects representing any violations found
     */
    public List<ReportingLineViolation> analyze(List<Employee> employees, Map<Integer, Employee> employeeById) {
        List<ReportingLineViolation> violations = new ArrayList<>();
        for (Employee employee : employees) {
            int managerCount = countManagersBetweenEmployeeAndCeo(employee, employeeById);
            if (managerCount > MAX_MANAGERS_ALLOWED) {
                int excessManagers = managerCount - MAX_MANAGERS_ALLOWED;
                violations.add(new ReportingLineViolation(employee, excessManagers));
            }
        }
        return violations;
    }

    /**
     * Counts the number of managers between the given employee and the CEO.
     *
     * @param employee     the Employee object for which to count the managers
     * @param employeeById a map of employee IDs to Employee objects for quick lookup
     * @return the number of managers between the given employee and the CEO
     */
    private int countManagersBetweenEmployeeAndCeo(Employee employee, Map<Integer, Employee> employeeById) {
        int managerCount = 0;
        Employee currentManager = employeeById.get(employee.managerId());
        while (Objects.nonNull(currentManager) && Objects.nonNull(currentManager.managerId())) {
            managerCount++;
            currentManager = employeeById.get(currentManager.managerId());
        }
        return managerCount;
    }
}