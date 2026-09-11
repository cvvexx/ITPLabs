package edu.course.lab03.priority;

import edu.course.lab03.task.ProjectTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DeadlinePriorityPolicy implements PriorityPolicy {

    private final LocalDateTime now;

    public DeadlinePriorityPolicy(LocalDateTime now) {
        if (now == null) {
            throw new IllegalArgumentException("now cannot be null");
        }
        this.now = now;
    }

    @Override
    public int calculatePriority(ProjectTask task) {
        long hoursLeft = ChronoUnit.HOURS.between(now, task.getDeadline());
        return Math.negateExact(Math.toIntExact(hoursLeft));
    }
}
