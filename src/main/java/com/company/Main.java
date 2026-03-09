package com.company;

import com.company.employee.model.Employee;
import com.company.employee.model.ReportingLineViolation;
import com.company.employee.model.SalaryViolation;
import com.company.employee.model.ViolationType;
import com.company.employee.reader.EmployeeFileReader;
import com.company.employee.service.EmployeeHierarchyService;
import com.company.employee.service.ReportingLineAnalyzer;
import com.company.employee.service.SalaryAnalyzer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        EmployeeFileReader employeeFileReader = new EmployeeFileReader();
        EmployeeHierarchyService employeeHierarchyService = new EmployeeHierarchyService();
        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();

        try {
            List<Employee> employees = employeeFileReader.readEmployees("employees.csv");
            Map<Integer, Employee> employeeById = employeeHierarchyService.buildEmployeeByIdMap(employees);
            Map<Integer, List<Employee>> subordinatesByManagerId = employeeHierarchyService.buildSubordinatesByManagerIdMap(employees);
            List<SalaryViolation> salaryViolations = salaryAnalyzer.analyze(subordinatesByManagerId, employeeById);

            System.out.println("Managers with salary violations:");
            if (salaryViolations.isEmpty()) {
                System.out.println("No managers have salary violations.");
            } else {
                for (SalaryViolation violation : salaryViolations) {
                    String messageType = violation.violationType() == ViolationType.TOO_LOW
                            ? "earns less than required by " : "earns more than allowed by ";
                    System.out.println(
                            violation.manager().getFullName() + " " + messageType + String.format("%.2f", violation.amount())
                    );
                }
            }

            List<ReportingLineViolation> reportingLineViolations = reportingLineAnalyzer.analyze(employees, employeeById);
            System.out.println();
            System.out.println("Employees with reporting line too long:");
            if (reportingLineViolations.isEmpty()) {
                System.out.println("No employees have a reporting line that is too long.");
            } else {
                for (ReportingLineViolation violation : reportingLineViolations) {
                    System.out.println(
                            violation.employee().getFullName() +
                                    " has reporting line too long by " + violation.excessManagers() + " level(s)"
                    );
                }
            }
        } catch (IOException exception) {
            System.out.println("Error reading file: " + exception.getMessage());
        }
    }
}