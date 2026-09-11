package edu.course.lab03;

import edu.course.lab03.priority.DeadlinePriorityPolicy;
import edu.course.lab03.priority.EffortPriorityPolicy;
import edu.course.lab03.priority.PriorityPolicy;
import edu.course.lab03.priority.StatusPriorityPolicy;
import edu.course.lab03.task.ProjectTask;
import edu.course.lab03.task.TaskId;
import edu.course.lab03.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskPrioritizerTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 9, 11, 12, 0);

    private ProjectTask quickTask;
    private ProjectTask startedTask;

    @BeforeEach
    void setUp() {
        quickTask = new ProjectTask(new TaskId("TASK-1"), "Fix typo", TaskStatus.CREATED, 2, NOW.plusHours(24));
        startedTask = new ProjectTask(new TaskId("TASK-2"), "Write module", TaskStatus.PENDING, 20, NOW.plusHours(72));
    }

    @Test
    @DisplayName("При priorityPolicy = null бросаем исключение")
    void constructor_whenPriorityPolicyIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));
        assertEquals("priorityPolicy cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("При передаче null вместо задачи бросаем исключение")
    void prioritizeTask_whenTaskIsNull_shouldThrowIllegalArgumentException() {
        TaskPrioritizer taskPrioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        IllegalArgumentException firstException
                = assertThrows(IllegalArgumentException.class, () -> taskPrioritizer.prioritizeTask(null, quickTask));
        IllegalArgumentException secondException
                = assertThrows(IllegalArgumentException.class, () -> taskPrioritizer.prioritizeTask(quickTask, null));
        assertEquals("tasks cannot be null", firstException.getMessage());
        assertEquals("tasks cannot be null", secondException.getMessage());
    }

    @Test
    @DisplayName("Выбирается более приоритетная задача независимо от порядка аргументов")
    void prioritizeTask_whenPrioritiesDiffer_shouldReturnHigherPriorityTask() {
        TaskPrioritizer taskPrioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertSame(quickTask, taskPrioritizer.prioritizeTask(quickTask, startedTask));
        assertSame(quickTask, taskPrioritizer.prioritizeTask(startedTask, quickTask));
    }

    @Test
    @DisplayName("При равном приоритете выбирается первая задача")
    void prioritizeTask_whenPrioritiesAreEqual_shouldReturnFirstTask() {
        PriorityPolicy samePriority = task -> 0;
        TaskPrioritizer taskPrioritizer = new TaskPrioritizer(samePriority);
        assertSame(startedTask, taskPrioritizer.prioritizeTask(startedTask, quickTask));
    }

    @Test
    @DisplayName("Замена политики меняет выбор без изменения TaskPrioritizer")
    void prioritizeTask_whenPolicyIsReplaced_shouldUseNewPolicy() {
        TaskPrioritizer byEffort = new TaskPrioritizer(new EffortPriorityPolicy());
        TaskPrioritizer byStatus = new TaskPrioritizer(new StatusPriorityPolicy());
        TaskPrioritizer byDeadline = new TaskPrioritizer(new DeadlinePriorityPolicy(NOW));
        assertSame(quickTask, byEffort.prioritizeTask(quickTask, startedTask));
        assertSame(startedTask, byStatus.prioritizeTask(quickTask, startedTask));
        assertSame(quickTask, byDeadline.prioritizeTask(quickTask, startedTask));
    }
}
