package edu.course.lab05.task;

public record TaskId(String value) {

    public TaskId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
    }
}
