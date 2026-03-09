package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.SalaryViolation;
import com.company.employee.model.ViolationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SalaryAnalyzer {

    private static final double MIN_SALARY_MULTIPLIER = 1.2;
    private static final double MAX_SALARY_MULTIPLIER = 1.5;

    /**
     * Analyzes the salaries of managers compared to their subordinates and identifies any violations.
     *
     * @param subordinatesByManagerId a map where the key is the manager's ID and the value is a list of their subordinates
     * @param employeeById            a map where the key is the employee's ID and the value is the Employee object
     * @return a list of SalaryViolation objects representing any salary violations found
     */
    public List<SalaryViolation> analyze(Map<Integer, List<Employee>> subordinatesByManagerId, Map<Integer, Employee> employeeById) {
        List<SalaryViolation> violations = new ArrayList<>();

        for (Map.Entry<Integer, List<Employee>> entry : subordinatesByManagerId.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();
            Employee manager = employeeById.get(managerId);
            if (Objects.isNull(manager)|| subordinates.isEmpty()) {
                continue;
            }
            double averageSubordinateSalary = calculateAverageSalary(subordinates);
            double minimumAllowedSalary = averageSubordinateSalary * MIN_SALARY_MULTIPLIER;
            double maximumAllowedSalary = averageSubordinateSalary * MAX_SALARY_MULTIPLIER;
            if (manager.salary() < minimumAllowedSalary) {
                violations.add(new SalaryViolation(manager, minimumAllowedSalary - manager.salary(), ViolationType.TOO_LOW
                ));
            } else if (manager.salary() > maximumAllowedSalary) {
                violations.add(new SalaryViolation(manager, manager.salary() - maximumAllowedSalary, ViolationType.TOO_HIGH
                ));
            }
        }
        return violations;
    }

    /**
     * Calculates the average salary of a list of employees.
     *
     * @param employees the list of Employee objects to calculate the average salary for
     * @return the average salary of the provided employees
     */
    private double calculateAverageSalary(List<Employee> employees) {
        double totalSalary = 0.0;
        for (Employee employee : employees) {
            totalSalary += employee.salary();
        }
        return totalSalary / employees.size();
    }
}