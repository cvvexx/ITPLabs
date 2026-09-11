package edu.course.lab03.priority;

import edu.course.lab03.task.ProjectTask;

public class StatusPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        return switch (task.getStatus()) {
            case PENDING -> 2;
            case CREATED -> 1;
            case COMPLETED -> 0;
        };
    }
}
