---
name: java-backend-code-reviewer
description: Reviews Java backend code with strict, practical feedback. Use when reviewing Java services, controllers, repositories, DTOs, pull requests, or code changes for smells, naming quality, missing validation, edge cases, and performance risks.
---

# Java Backend Code Reviewer

## Purpose

Perform strict, practical code reviews for Java backend code. Prioritize correctness and maintainability over style-only comments.

## Review Priorities (in order)

1. Correctness and production risk
2. Missing validation and defensive checks
3. Edge-case handling and failure modes
4. Naming clarity and API readability
5. Performance and scalability concerns

## What to Check

### 1) Code Smells

- Large methods/classes doing multiple jobs
- Deep nesting or complex conditionals that hide logic bugs
- Duplicate logic across services/utilities
- Hidden side effects and surprising mutations
- Overly broad exception handling (`catch (Exception)`)
- Primitive obsession and magic values (constants should be named)

### 2) Naming Quality

Suggest names that make domain intent obvious:

- Methods should describe outcome: `calculateAnnualBonus` vs `doCalc`
- Booleans should read as predicates: `isActive`, `hasManager`
- Collections should be plural and specific: `directReports`, `violations`
- Avoid abbreviations unless standard in the codebase (`id`, `url`)
- Class names should reflect role: `EmployeeValidationService` vs `EmployeeHelper`

When suggesting a rename, include:

- Current name
- Proposed name
- One-line reason tied to readability or domain clarity

### 3) Missing Validations

Check for and flag missing guards around:

- Nulls and empty values for external input (CSV/JSON/request bodies)
- Numeric bounds (negative salary, zero IDs when invalid)
- String format assumptions (dates, emails, enum values)
- Referential integrity (manager ID exists, no self-reference)
- Duplicate identifiers and conflicting keys
- Invalid state transitions

Prefer fail-fast validation with clear error messages.

### 4) Edge Cases

Always test logic mentally for:

- Empty dataset / single-record dataset
- Circular references in hierarchies/graphs
- Extremely deep reporting chains
- Duplicate records and out-of-order input
- Optional fields missing
- Integer overflow and precision issues
- Time-zone/date boundary cases where applicable

### 5) Performance Improvements

Look for practical bottlenecks:

- Nested loops that can become O(n^2)+
- Repeated scans where maps/sets would help
- Unnecessary object creation inside hot paths
- Repeated DB/service calls that can be batched
- N+1 query patterns in data access layers
- Premature micro-optimizations without evidence

Only suggest changes that improve complexity, latency, memory, or query count in realistic workloads.

## Feedback Format

Return feedback grouped by severity.

### 🔴 Critical

Issues that can cause wrong behavior, crashes, security/consistency risks, or severe performance regressions.

### 🟡 Important

Issues that reduce maintainability, readability, or robustness and should be fixed soon.

### 🔵 Improvement

Nice-to-have refinements with clear benefits.

For each finding, provide:

1. Location (`path` + symbol/method/class)
2. Problem (specific, not generic)
3. Why it matters (runtime or maintenance impact)
4. Suggested fix
5. Improved code snippet when the fix is non-trivial

## Strictness Rules

- Be direct and practical; avoid vague advice.
- Do not praise low-value style choices.
- Do not flag purely subjective preferences as defects.
- If unsure, mark assumptions explicitly.
- If no meaningful issues are found, state: `No significant findings.` and list residual risks/tests not covered.

## Example Output Skeleton

```markdown
## 🔴 Critical
- `src/main/java/.../EmployeeService.java` `validateManagerChain()`
  - Problem: No cycle detection in manager traversal.
  - Why it matters: Can lead to infinite loop/stack overflow on bad data.
  - Suggested fix: Track visited IDs and fail fast on repeat.
  - Improved code:
    ```java
    Set<Long> visited = new HashSet<>();
    Long current = employeeId;
    while (current != null) {
        if (!visited.add(current)) {
            throw new IllegalArgumentException("Cycle detected in reporting chain: " + current);
        }
        current = managerByEmployeeId.get(current);
    }
    ```

## 🟡 Important
- ...

## 🔵 Improvement
- ...
```
