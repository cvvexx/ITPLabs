package edu.course.lab02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTaskTest {

    private ProjectTask projectTask;

    @BeforeEach
    void setUp() {
        projectTask = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8);
    }

    @Test
    @DisplayName("Корректная задача создается с переданными значениями")
    void constructor_whenValidArguments_shouldCreateTask() {
        assertEquals(new TaskId("TASK-1"), projectTask.getId());
        assertEquals("Write tests", projectTask.getTitle());
        assertEquals(TaskStatus.CREATED, projectTask.getStatus());
        assertEquals(8, projectTask.getEstimatedHours());
    }

    @Test
    @DisplayName("При id = null бросаем исключение")
    void constructor_whenIdIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(null, "Write tests", TaskStatus.CREATED, 8));
        assertEquals("id cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("При пустом title(null, пустая строка, пробелы) бросаем исключение")
    void constructor_whenTitleIsNullOrBlank_shouldThrowIllegalArgumentException(String title) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(new TaskId("TASK-1"), title, TaskStatus.CREATED, 8));
        assertEquals("title cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("При status = null бросаем исключение")
    void constructor_whenStatusIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(new TaskId("TASK-1"), "Write tests", null, 8));
        assertEquals("status cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0})
    @DisplayName("При невалидной оценке трудоемкости(<=0) бросаем исключение")
    void constructor_whenEstimatedHoursIsNotPositive_shouldThrowIllegalArgumentException(int hours) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, hours));
        assertEquals("estimatedHours must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Статус задачи меняется")
    void changeStatus_whenNewStatusIsPending_statusShouldBePending() {
        projectTask.changeStatus(TaskStatus.PENDING);
        assertEquals(TaskStatus.PENDING, projectTask.getStatus());
    }

    @Test
    @DisplayName("При попытке сменить статус на null бросаем исключение")
    void changeStatus_whenNewStatusIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> projectTask.changeStatus(null));
        assertEquals("status cannot be null", exception.getMessage());
        assertEquals(TaskStatus.CREATED, projectTask.getStatus());
    }

    @Test
    @DisplayName("При попытке установить уже текущий статус бросаем исключение")
    void changeStatus_whenNewStatusIsSameAsCurrent_shouldThrowIllegalStateException() {
        IllegalStateException exception
                = assertThrows(IllegalStateException.class, () -> projectTask.changeStatus(TaskStatus.CREATED));
        assertEquals("this status is already set", exception.getMessage());
        assertEquals(TaskStatus.CREATED, projectTask.getStatus());
    }

    @Test
    @DisplayName("Задача со статусом COMPLETED считается завершенной")
    void isCompleted_whenStatusIsCompleted_shouldReturnTrue() {
        projectTask.changeStatus(TaskStatus.COMPLETED);
        assertTrue(projectTask.isCompleted());
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREATED", "PENDING"})
    @DisplayName("Задача с любым статусом, кроме COMPLETED, не считается завершенной")
    void isCompleted_whenStatusIsNotCompleted_shouldReturnFalse(TaskStatus status) {
        ProjectTask task = new ProjectTask(new TaskId("TASK-2"), "Review code", status, 8);
        assertFalse(task.isCompleted());
    }

    @Test
    @DisplayName("Оценка трудоемкости увеличивается")
    void increaseEstimate_whenAdded4Hours_estimatedHoursShouldBe12() {
        projectTask.increaseEstimate(4);
        assertEquals(12, projectTask.getEstimatedHours());
    }

    @ParameterizedTest
    @ValueSource(ints = {-4, 0})
    @DisplayName("При попытке увеличить оценку на невалидное число часов(<=0) бросаем исключение")
    void increaseEstimate_whenHoursIsNotPositive_shouldThrowIllegalArgumentException(int hours) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> projectTask.increaseEstimate(hours));
        assertEquals("hours must be positive", exception.getMessage());
        assertEquals(8, projectTask.getEstimatedHours());
    }

    @Test
    @DisplayName("При переполнении оценки трудоемкости бросаем исключение")
    void increaseEstimate_whenResultOverflowsInt_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> projectTask.increaseEstimate(Integer.MAX_VALUE));
        assertEquals("estimatedHours is too large", exception.getMessage());
        assertEquals(8, projectTask.getEstimatedHours());
    }
}
