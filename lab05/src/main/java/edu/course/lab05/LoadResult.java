package edu.course.lab05;

import edu.course.lab05.task.ProjectTask;

import java.util.List;

public record LoadResult(List<ProjectTask> tasks, List<ValidationError> errors) {

    public LoadResult(List<ProjectTask> tasks, List<ValidationError> errors) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks cannot be null");
        }
        if (errors == null) {
            throw new IllegalArgumentException("errors cannot be null");
        }
        this.tasks = List.copyOf(tasks);
        this.errors = List.copyOf(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public String toString() {
        return "LoadResult{tasks=" + tasks.size() + ", errors=" + errors.size() + "}";
    }
}
