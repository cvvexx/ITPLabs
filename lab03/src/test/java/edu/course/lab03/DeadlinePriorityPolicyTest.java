package edu.course.lab03;

import edu.course.lab03.priority.DeadlinePriorityPolicy;
import edu.course.lab03.task.ProjectTask;
import edu.course.lab03.task.TaskId;
import edu.course.lab03.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DeadlinePriorityPolicyTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 9, 11, 12, 0);

    private DeadlinePriorityPolicy deadlinePriorityPolicy;

    @BeforeEach
    void setUp() {
        deadlinePriorityPolicy = new DeadlinePriorityPolicy(NOW);
    }

    @Test
    @DisplayName("При now = null бросаем исключение")
    void constructor_whenNowIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new DeadlinePriorityPolicy(null));
        assertEquals("now cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Приоритет задачи со сроком через 5 часов равен -5")
    void calculatePriority_whenDeadlineIsIn5Hours_shouldReturnMinus5() {
        ProjectTask task = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8, NOW.plusHours(5));
        assertEquals(-5, deadlinePriorityPolicy.calculatePriority(task));
    }

    @Test
    @DisplayName("Задача с более ранним сроком имеет больший приоритет")
    void calculatePriority_whenDeadlineIsEarlier_shouldHaveHigherPriority() {
        ProjectTask urgentTask = new ProjectTask(new TaskId("TASK-1"), "Fix bug", TaskStatus.CREATED, 8, NOW.plusDays(1));
        ProjectTask laterTask = new ProjectTask(new TaskId("TASK-2"), "Write docs", TaskStatus.CREATED, 8, NOW.plusYears(1));
        assertTrue(deadlinePriorityPolicy.calculatePriority(urgentTask) > deadlinePriorityPolicy.calculatePriority(laterTask));
    }

    @Test
    @DisplayName("Просроченная задача имеет положительный приоритет, больший чем у задачи в срок")
    void calculatePriority_whenDeadlineIsOverdue_shouldReturnPositivePriority() {
        ProjectTask overdueTask = new ProjectTask(new TaskId("TASK-1"), "Fix bug", TaskStatus.CREATED, 8, NOW.minusHours(3));
        ProjectTask futureTask = new ProjectTask(new TaskId("TASK-2"), "Write docs", TaskStatus.CREATED, 8, NOW.plusHours(3));
        assertEquals(3, deadlinePriorityPolicy.calculatePriority(overdueTask));
        assertTrue(deadlinePriorityPolicy.calculatePriority(overdueTask) > deadlinePriorityPolicy.calculatePriority(futureTask));
    }

    @ParameterizedTest
    @ValueSource(longs = {2_147_483_648L, -2_147_483_648L})
    @DisplayName("При сроке, не помещающемся в int часов, бросаем исключение вместо переполнения")
    void calculatePriority_whenHoursLeftOverflowsInt_shouldThrowArithmeticException(long hours) {
        ProjectTask task = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8, NOW.plusHours(hours));
        assertThrows(ArithmeticException.class, () -> deadlinePriorityPolicy.calculatePriority(task));
    }
}
