package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.SalaryViolation;
import com.company.employee.model.ViolationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalaryAnalyzer {

    private static final double MIN_SALARY_MULTIPLIER = 1.2;
    private static final double MAX_SALARY_MULTIPLIER = 1.5;

    public List<SalaryViolation> analyze(Map<Integer, List<Employee>> subordinatesByManagerId,
                                         Map<Integer, Employee> employeeById) {
        List<SalaryViolation> violations = new ArrayList<>();

        for (Map.Entry<Integer, List<Employee>> entry : subordinatesByManagerId.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();

            Employee manager = employeeById.get(managerId);
            if (manager == null || subordinates.isEmpty()) {
                continue;
            }

            double averageSubordinateSalary = calculateAverageSalary(subordinates);
            double minimumAllowedSalary = averageSubordinateSalary * MIN_SALARY_MULTIPLIER;
            double maximumAllowedSalary = averageSubordinateSalary * MAX_SALARY_MULTIPLIER;

            if (manager.getSalary() < minimumAllowedSalary) {
                violations.add(new SalaryViolation(
                        manager,
                        minimumAllowedSalary - manager.getSalary(),
                        ViolationType.TOO_LOW
                ));
            } else if (manager.getSalary() > maximumAllowedSalary) {
                violations.add(new SalaryViolation(
                        manager,
                        manager.getSalary() - maximumAllowedSalary,
                        ViolationType.TOO_HIGH
                ));
            }
        }

        return violations;
    }

    private double calculateAverageSalary(List<Employee> employees) {
        double totalSalary = 0.0;

        for (Employee employee : employees) {
            totalSalary += employee.getSalary();
        }

        return totalSalary / employees.size();
    }
}