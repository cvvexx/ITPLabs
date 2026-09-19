package edu.course.lab07;

import edu.course.lab07.task.ProjectTask;
import edu.course.lab07.task.TaskStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataAnalytics {

    private DataAnalytics() {
    }

    public static List<ProjectTask> filterByStatus(List<ProjectTask> tasks, TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    public static List<String> getNormalizedCategories(List<ProjectTask> tasks) {
        return tasks.stream()
                .map(DataAnalytics::normalizeCategory)
                .distinct()
                .sorted()
                .toList();
    }

    public static Map<String, Long> countByCategory(List<ProjectTask> tasks) {
        return tasks.stream()
                .collect(
                        Collectors.groupingBy(
                                DataAnalytics::normalizeCategory,
                                Collectors.counting()
                        )
                );
    }

    public static Optional<ProjectTask> findMaxEstimatedHoursTask(List<ProjectTask> tasks) {
        return tasks.stream()
                .max(Comparator.comparingInt(ProjectTask::getEstimatedHours));
    }

    public static List<ProjectTask> sortTasksByTwoKeys(List<ProjectTask> tasks) {
        return tasks.stream()
                .sorted(
                        Comparator.comparing(DataAnalytics::normalizeCategory)
                                .thenComparing(ProjectTask::getTitle)
                )
                .toList();
    }

    //Доп задание
    public static List<String> getTopNCategories(List<ProjectTask> tasks, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        return countByCategory(tasks).entrySet().stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue().reversed()
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    private static String normalizeCategory(ProjectTask task) {
        return task.getCategory().trim().toLowerCase();
    }
}
