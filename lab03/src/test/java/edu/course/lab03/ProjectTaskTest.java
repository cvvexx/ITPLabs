package edu.course.lab03;

import edu.course.lab03.task.ProjectTask;
import edu.course.lab03.task.TaskId;
import edu.course.lab03.task.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTaskTest {

    private static final LocalDateTime DEADLINE = LocalDateTime.of(2026, 9, 15, 18, 0);

    @Test
    @DisplayName("Корректная задача создается с переданным сроком")
    void constructor_whenValidArguments_shouldCreateTaskWithDeadline() {
        ProjectTask projectTask = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8, DEADLINE);
        assertEquals(DEADLINE, projectTask.getDeadline());
    }

    @Test
    @DisplayName("При deadline = null бросаем исключение")
    void constructor_whenDeadlineIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8, null));
        assertEquals("deadline cannot be null", exception.getMessage());
    }
}
