package com.company.employee.service;

import com.company.employee.model.Employee;
import com.company.employee.model.ReportingLineViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportingLineAnalyzer {

    private static final int MAX_MANAGERS_ALLOWED = 4;

    public List<ReportingLineViolation> analyze(List<Employee> employees,
                                                Map<Integer, Employee> employeeById) {
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

    private int countManagersBetweenEmployeeAndCeo(Employee employee,
                                                   Map<Integer, Employee> employeeById) {
        int managerCount = 0;
        Integer managerId = employee.getManagerId();

        while (managerId != null) {
            managerCount++;
            Employee manager = employeeById.get(managerId);

            if (manager == null) {
                break;
            }

            managerId = manager.getManagerId();
        }

        return managerCount;
    }
}