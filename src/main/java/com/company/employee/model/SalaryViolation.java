package com.company.employee.model;

public record SalaryViolation(Employee manager, double amount, ViolationType violationType) {
}

