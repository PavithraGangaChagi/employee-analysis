package com.company;

import com.company.employee.model.Employee;
import com.company.employee.reader.EmployeeFileReader;
import com.company.employee.service.EmployeeHierarchyService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EmployeeFileReader reader = new EmployeeFileReader();
        EmployeeHierarchyService hierarchyService = new EmployeeHierarchyService();

        try {
            List<Employee> employees = reader.readEmployees("employees.csv");

            Map<Integer, Employee> employeeById = hierarchyService.buildEmployeeByIdMap(employees);
            Map<Integer, List<Employee>> subordinatesByManagerId =
                    hierarchyService.buildSubordinatesByManagerIdMap(employees);

            System.out.println("Total employees: " + employees.size());
            System.out.println("Employee map size: " + employeeById.size());
            System.out.println("Managers with subordinates: " + subordinatesByManagerId.size());
        } catch (IOException exception) {
            System.out.println("Error reading file: " + exception.getMessage());
        }
    }
}