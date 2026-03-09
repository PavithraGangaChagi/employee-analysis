package com.company;

import com.company.employee.model.Employee;
import com.company.employee.reader.EmployeeFileReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args)  {
        EmployeeFileReader reader = new EmployeeFileReader();
        try {
            List<Employee> employees = reader.readEmployees("employees.csv");
            System.out.println("Total employees: " + employees.size());
            for(Employee employee:employees){
                System.out.println("Total employees: " + employee.getFullName());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}