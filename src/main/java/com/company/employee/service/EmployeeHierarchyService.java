package com.company.employee.service;

import com.company.employee.model.Employee;

import java.util.*;

public class EmployeeHierarchyService {

    /**
     * Builds a map of employee IDs to Employee objects for quick lookup.
     *
     * @param employees the list of Employee objects to process
     * @return a map where the key is the employee ID and the value is the corresponding Employee object
     */
    public Map<Integer, Employee> buildEmployeeByIdMap(List<Employee> employees) {
        Map<Integer, Employee> employeeById = new HashMap<>();
        for (Employee employee : employees) {
            employeeById.put(employee.id(), employee);
        }
        return employeeById;
    }

    /**
     * Builds a map of manager IDs to lists of their direct subordinates.
     *
     * @param employees the list of Employee objects to process
     * @return a map where the key is the manager ID and the value is a list of Employee objects that report to that manager
     */
    public Map<Integer, List<Employee>> buildSubordinatesByManagerIdMap(List<Employee> employees) {
        Map<Integer, List<Employee>> subordinatesByManagerId = new HashMap<>();
        for (Employee employee : employees) {
            Integer managerId = employee.managerId();
            if (Objects.nonNull(managerId)) {
                subordinatesByManagerId.computeIfAbsent(managerId, key -> new ArrayList<>()).add(employee);
            }
        }
        return subordinatesByManagerId;
    }
}