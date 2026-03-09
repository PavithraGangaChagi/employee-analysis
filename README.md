# Employee Analysis

## Overview
This application reads employee data from a CSV file, analyzes the organizational structure, and reports:

- managers who earn less than they should
- managers who earn more than they should
- employees whose reporting line is too long

## Assumptions
- There is exactly one CEO
- CEO has no `managerId`
- Salary checks apply only to managers with at least one direct subordinate
- Reporting line counts managers between the employee and the CEO, not the CEO
- The input CSV format is fixed as:

```csv
Id,firstName,lastName,salary,managerId