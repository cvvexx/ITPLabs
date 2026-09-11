package edu.course.lab03;

import edu.course.lab03.priority.PriorityPolicy;
import edu.course.lab03.task.ProjectTask;

public class TaskPrioritizer {

    private final PriorityPolicy priorityPolicy;

    public TaskPrioritizer(PriorityPolicy priorityPolicy) {
        if (priorityPolicy == null) {
            throw new IllegalArgumentException("priorityPolicy cannot be null");
        }
        this.priorityPolicy = priorityPolicy;
    }

    public ProjectTask prioritizeTask(ProjectTask firstTask, ProjectTask secondTask) {
        if (firstTask == null || secondTask == null) {
            throw new IllegalArgumentException("tasks cannot be null");
        }
        int firstPriority = priorityPolicy.calculatePriority(firstTask);
        int secondPriority = priorityPolicy.calculatePriority(secondTask);
        return firstPriority >= secondPriority ? firstTask : secondTask;
    }

}
