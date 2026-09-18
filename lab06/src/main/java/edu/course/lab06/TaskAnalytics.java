package edu.course.lab06;

import edu.course.lab06.task.ProjectTask;
import edu.course.lab06.task.TaskStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class TaskAnalytics {

    private final List<ProjectTask> tasks;

    public TaskAnalytics(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks cannot be null");
        }
        for (ProjectTask task : tasks) {
            if (task == null) {
                throw new IllegalArgumentException("tasks cannot contain null");
            }
        }
        this.tasks = new ArrayList<>(tasks);
    }

    public Map<TaskStatus, Integer> countByStatus() {
        Map<TaskStatus, Integer> counts = new EnumMap<>(TaskStatus.class);
        for (ProjectTask task : tasks) {
            TaskStatus status = task.getStatus();
            Integer current = counts.get(status);
            if (current == null) {
                counts.put(status, 1);
            } else {
                counts.put(status, current + 1);
            }
        }
        return counts;
    }

    public Set<String> uniqueCategories() {
        Set<String> categories = new TreeSet<>();
        for (ProjectTask task : tasks) {
            categories.add(task.getCategory());
        }
        return categories;
    }

    public List<ProjectTask> sortedByEffortAndId() {
        List<ProjectTask> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparingInt(ProjectTask::getEstimatedHours)
                .thenComparing(task -> task.getId().value()));
        return sorted;
    }

    public int totalEstimatedHours() {
        int total = 0;
        for (ProjectTask task : tasks) {
            total += task.getEstimatedHours();
        }
        return total;
    }

    public int size() {
        return tasks.size();
    }
}
