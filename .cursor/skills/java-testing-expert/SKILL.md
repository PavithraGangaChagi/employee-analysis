---
name: java-testing-expert
description: Generates robust JUnit 5 tests for Java backend code. Use when creating or improving unit tests, adding edge-case coverage, writing negative scenarios, or increasing test coverage with clear assertions and maintainable structure.
---

# Java Testing Expert

## Purpose

Generate high-quality JUnit 5 tests that are readable, deterministic, and focused on behavior.

## Default Output Goals

For every testing task:

1. Cover normal behavior first (happy path)
2. Add edge cases (boundaries, empty/null, extremes)
3. Add negative scenarios (invalid inputs, failures, exceptions)
4. Maximize meaningful coverage, not assertion count
5. Keep tests clear and maintainable

## Test Structure Rules

- Use JUnit 5 (`org.junit.jupiter.api`).
- Prefer one behavioral expectation per test method.
- Name tests with intent using `given_when_then` style.
- Use Arrange / Act / Assert sections with blank-line separation.
- Keep setup minimal and local to each test unless shared setup is truly repeated.
- Use `@Nested` to group related behavior when useful.
- Use `@ParameterizedTest` for repeated input permutations.

## Assertions and Clarity

- Prefer specific assertions: `assertEquals`, `assertTrue`, `assertFalse`, `assertThrows`.
- For exception checks, assert both type and message/content when stable.
- Avoid weak assertions (for example: only checking non-null when stronger checks are possible).
- Assert observable behavior, not private implementation details.
- When assertions are complex, extract helper methods with clear names.

## Edge Case Checklist

Always evaluate and include relevant cases:

- Empty input collections/strings
- Null values (if API allows or must reject)
- Minimum/maximum boundary values
- Single-item vs multi-item inputs
- Duplicate or conflicting values
- Unsorted or unexpected ordering
- Large input size where complexity risks exist

## Negative Scenario Checklist

Always test failure behavior where applicable:

- Invalid argument formats
- Illegal state transitions
- Missing required dependencies/data
- Non-existent IDs/references
- Exception propagation and wrapping behavior
- Validation error messages

## Mocking and Isolation

- Prefer real objects for simple domain models.
- Mock external collaborators only (repositories, gateways, clients, clocks, random sources).
- Do not over-mock value objects or pure logic.
- Verify interactions only when behavior requires it; avoid brittle interaction-heavy tests.

## Coverage Guidance

- Target branch and decision coverage for core business logic.
- Ensure each conditional path has at least one test.
- Include regression tests for discovered bugs before/with fixes.
- Avoid redundant tests that do not add new behavioral confidence.

## Output Format

When generating tests, include:

1. Brief list of tested scenarios (happy, edge, negative)
2. Full JUnit 5 test class code
3. Optional short note on uncovered risks if any remain

## Example Test Skeleton

```java
@DisplayName("SalaryAnalyzer")
class SalaryAnalyzerTest {

    @Test
    void givenValidEmployees_whenAnalyze_thenReturnsNoViolations() {
        // Arrange
        List<Employee> employees = List.of(
            new Employee(1L, "CEO", 5_000, null),
            new Employee(2L, "Dev", 2_000, 1L)
        );

        SalaryAnalyzer analyzer = new SalaryAnalyzer();

        // Act
        List<SalaryViolation> violations = analyzer.analyze(employees);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullInput_whenAnalyze_thenThrowsIllegalArgumentException() {
        // Arrange
        SalaryAnalyzer analyzer = new SalaryAnalyzer();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> analyzer.analyze(null)
        );
        assertTrue(ex.getMessage().contains("employees"));
    }
}
```
