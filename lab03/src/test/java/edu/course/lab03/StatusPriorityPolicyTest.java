package edu.course.lab03;

import edu.course.lab03.priority.StatusPriorityPolicy;
import edu.course.lab03.task.ProjectTask;
import edu.course.lab03.task.TaskId;
import edu.course.lab03.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatusPriorityPolicyTest {

    private static final LocalDateTime DEADLINE = LocalDateTime.of(2026, 9, 15, 18, 0);

    private StatusPriorityPolicy statusPriorityPolicy;

    @BeforeEach
    void setUp() {
        statusPriorityPolicy = new StatusPriorityPolicy();
    }

    @ParameterizedTest
    @CsvSource({"PENDING, 2", "CREATED, 1", "COMPLETED, 0"})
    @DisplayName("Каждому статусу соответствует свой приоритет")
    void calculatePriority_whenStatusIsGiven_shouldReturnExpectedPriority(TaskStatus status, int expected) {
        ProjectTask task = new ProjectTask(new TaskId("TASK-1"), "Write tests", status, 8, DEADLINE);
        assertEquals(expected, statusPriorityPolicy.calculatePriority(task));
    }

    @Test
    @DisplayName("Начатая задача приоритетнее новой, а новая приоритетнее завершенной")
    void calculatePriority_whenStatusesDiffer_shouldOrderPendingCreatedCompleted() {
        ProjectTask pendingTask = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.PENDING, 8, DEADLINE);
        ProjectTask createdTask = new ProjectTask(new TaskId("TASK-2"), "Review code", TaskStatus.CREATED, 8, DEADLINE);
        ProjectTask completedTask = new ProjectTask(new TaskId("TASK-3"), "Setup CI", TaskStatus.COMPLETED, 8, DEADLINE);
        assertTrue(statusPriorityPolicy.calculatePriority(pendingTask) > statusPriorityPolicy.calculatePriority(createdTask));
        assertTrue(statusPriorityPolicy.calculatePriority(createdTask) > statusPriorityPolicy.calculatePriority(completedTask));
    }
}
