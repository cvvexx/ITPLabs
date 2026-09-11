package edu.course.lab03;

import edu.course.lab03.priority.EffortPriorityPolicy;
import edu.course.lab03.task.ProjectTask;
import edu.course.lab03.task.TaskId;
import edu.course.lab03.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EffortPriorityPolicyTest {

    private static final LocalDateTime DEADLINE = LocalDateTime.of(2026, 9, 15, 18, 0);

    private EffortPriorityPolicy effortPriorityPolicy;

    @BeforeEach
    void setUp() {
        effortPriorityPolicy = new EffortPriorityPolicy();
    }

    @Test
    @DisplayName("Приоритет задачи на 8 часов равен -8")
    void calculatePriority_whenTaskIs8Hours_shouldReturnMinus8() {
        ProjectTask task = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8, DEADLINE);
        assertEquals(-8, effortPriorityPolicy.calculatePriority(task));
    }

    @Test
    @DisplayName("Задача с меньшей трудоемкостью имеет больший приоритет")
    void calculatePriority_whenTaskHasFewerHours_shouldHaveHigherPriority() {
        ProjectTask quickTask = new ProjectTask(new TaskId("TASK-1"), "Fix typo", TaskStatus.CREATED, 2, DEADLINE);
        ProjectTask longTask = new ProjectTask(new TaskId("TASK-2"), "Write module", TaskStatus.CREATED, 10, DEADLINE);
        assertTrue(effortPriorityPolicy.calculatePriority(quickTask) > effortPriorityPolicy.calculatePriority(longTask));
    }
}
