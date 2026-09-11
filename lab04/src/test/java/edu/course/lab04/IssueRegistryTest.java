package edu.course.lab04;

import edu.course.lab04.task.ProjectTask;
import edu.course.lab04.task.TaskId;
import edu.course.lab04.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueRegistryTest {

    private IssueRegistry issueRegistry;
    private ProjectTask firstTask;
    private ProjectTask secondTask;

    @BeforeEach
    void setUp() {
        issueRegistry = new IssueRegistry();
        firstTask = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8);
        secondTask = new ProjectTask(new TaskId("TASK-2"), "Review code", TaskStatus.PENDING, 4);
    }

    @Test
    @DisplayName("Добавленная задача находится по ключу")
    void addTask_whenTaskAdded_shouldBeFoundByKey() {
        IssueId issueId = new IssueId(7, 42);
        issueRegistry.addTask(issueId, firstTask);

        assertSame(firstTask, issueRegistry.getTask(issueId));
        assertTrue(issueRegistry.containsTask(issueId));
        assertEquals(1, issueRegistry.size());
    }

    @Test
    @DisplayName("Два разных экземпляра логически равного ключа находят одну задачу")
    void getTask_whenKeyIsAnotherEqualInstance_shouldReturnSameTask() {
        IssueId issueId = new IssueId(7, 42);
        IssueId sameIssueId = new IssueId(7, 42);
        issueRegistry.addTask(issueId, firstTask);

        assertNotSame(issueId, sameIssueId);
        assertSame(firstTask, issueRegistry.getTask(sameIssueId));
        assertTrue(issueRegistry.containsTask(sameIssueId));
    }

    @Test
    @DisplayName("Повторное добавление по равному ключу заменяет задачу и не увеличивает размер")
    void addTask_whenKeyIsEqual_shouldReplaceTaskAndKeepSize() {
        issueRegistry.addTask(new IssueId(7, 42), firstTask);
        issueRegistry.addTask(new IssueId(7, 42), secondTask);

        assertSame(secondTask, issueRegistry.getTask(new IssueId(7, 42)));
        assertEquals(1, issueRegistry.size());
    }

    @Test
    @DisplayName("Неравные ключи с одинаковым hashCode хранят разные задачи")
    void addTask_whenKeysCollide_shouldKeepBothTasks() {
        IssueId first = new IssueId(1, 0);
        IssueId second = new IssueId(0, 31);
        issueRegistry.addTask(first, firstTask);
        issueRegistry.addTask(second, secondTask);

        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(2, issueRegistry.size());
        assertSame(firstTask, issueRegistry.getTask(first));
        assertSame(secondTask, issueRegistry.getTask(second));
    }

    @Test
    @DisplayName("Удаленная задача больше не находится, размер уменьшается")
    void removeTask_whenTaskExists_shouldRemoveIt() {
        IssueId issueId = new IssueId(7, 42);
        issueRegistry.addTask(issueId, firstTask);

        issueRegistry.removeTask(new IssueId(7, 42));

        assertNull(issueRegistry.getTask(issueId));
        assertFalse(issueRegistry.containsTask(issueId));
        assertEquals(0, issueRegistry.size());
    }

    @Test
    @DisplayName("Поиск по отсутствующему ключу возвращает null и false")
    void getTask_whenKeyIsAbsent_shouldReturnNull() {
        issueRegistry.addTask(new IssueId(7, 42), firstTask);

        assertNull(issueRegistry.getTask(new IssueId(7, 43)));
        assertFalse(issueRegistry.containsTask(new IssueId(7, 43)));
    }

    @Test
    @DisplayName("Пустой реестр имеет размер 0")
    void size_whenRegistryIsEmpty_shouldReturnZero() {
        assertEquals(0, issueRegistry.size());
    }
}
