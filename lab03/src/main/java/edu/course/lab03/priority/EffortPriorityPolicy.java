package edu.course.lab03.priority;

import edu.course.lab03.task.ProjectTask;

public class EffortPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        return -task.getEstimatedHours(); //чем меньше часов таска, тем выше приоритет
    }
}
