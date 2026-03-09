package com.company.employee.model;

public class ReportingLineViolation {

    private final Employee employee;
    private final int excessManagers;

    public ReportingLineViolation(Employee employee, int excessManagers) {
        this.employee = employee;
        this.excessManagers = excessManagers;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getExcessManagers() {
        return excessManagers;
    }
}