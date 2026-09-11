package edu.course.lab04;

import edu.course.lab04.task.ProjectTask;
import edu.course.lab04.task.TaskId;
import edu.course.lab04.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MutableKeyExperimentTest {

    private Map<MutableIssueKey, ProjectTask> tasks;
    private MutableIssueKey key;
    private ProjectTask task;

    @BeforeEach
    void setUp() {
        tasks = new HashMap<>();
        key = new MutableIssueKey(7, 42);
        task = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8);
        tasks.put(key, task);
    }

    @Test
    @DisplayName("До изменения ключа задача находится")
    void get_whenKeyIsNotChanged_shouldReturnTask() {
        assertSame(task, tasks.get(key));
        assertSame(task, tasks.get(new MutableIssueKey(7, 42)));
    }

    @Test
    @DisplayName("После изменения поля ключа задача не находится ни по тому же объекту, ни по старым значениям")
    void get_whenKeyIsChangedAfterPut_shouldNotFindTask() {
        key.setValue(43);

        assertNull(tasks.get(key));
        assertFalse(tasks.containsKey(key));
        assertNull(tasks.get(new MutableIssueKey(7, 42)));
        assertEquals(1, tasks.size());
    }
}
