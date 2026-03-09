package com.company.employee.model;

public class SalaryViolation {

    private final Employee manager;
    private final double amount;
    private final ViolationType violationType;

    public SalaryViolation(Employee manager, double amount, ViolationType violationType) {
        this.manager = manager;
        this.amount = amount;
        this.violationType = violationType;
    }

    public Employee getManager() {
        return manager;
    }

    public double getAmount() {
        return amount;
    }

    public ViolationType getViolationType() {
        return violationType;
    }
}