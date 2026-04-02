package com.company.employee.reader;

public class MalformedEmployeeRowException extends RuntimeException {
    public MalformedEmployeeRowException(String message) {
        super(message);
    }

    public MalformedEmployeeRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
