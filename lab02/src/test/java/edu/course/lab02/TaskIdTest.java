package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TaskIdTest {

    @Test
    @DisplayName("Корректный идентификатор хранит переданное значение")
    void constructor_whenValidValue_shouldCreateTaskId() {
        TaskId taskId = new TaskId("TASK-1");
        assertEquals("TASK-1", taskId.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("При пустом значении(null, пустая строка, пробелы) бросаем исключение")
    void constructor_whenValueIsNullOrBlank_shouldThrowIllegalArgumentException(String value) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new TaskId(value));
        assertEquals("id cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Идентификаторы с одинаковым значением равны")
    void equals_whenValuesAreSame_shouldBeEqual() {
        TaskId first = new TaskId("TASK-1");
        TaskId second = new TaskId("TASK-1");
        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("Идентификаторы с разными значениями не равны")
    void equals_whenValuesAreDifferent_shouldNotBeEqual() {
        assertNotEquals(new TaskId("TASK-1"), new TaskId("TASK-2"));
    }
}
