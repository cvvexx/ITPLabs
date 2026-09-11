package edu.course.lab03.priority;

import edu.course.lab03.task.ProjectTask;

public interface PriorityPolicy {
    int calculatePriority(ProjectTask task);
}
