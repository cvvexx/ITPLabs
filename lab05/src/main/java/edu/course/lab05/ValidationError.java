package edu.course.lab05;

public record ValidationError(int lineNumber, String field, String message) {

    public ValidationError {
        if (lineNumber <= 0) {
            throw new IllegalArgumentException("lineNumber must be positive");
        }
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("field cannot be null or blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return "line " + lineNumber + ", field '" + field + "': " + message;
    }
}
