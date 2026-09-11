package edu.course.lab04;

import edu.course.lab04.task.ProjectTask;
import edu.course.lab04.task.TaskId;
import edu.course.lab04.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ChainedHashTableTest {

    private record FixedHashKey(String name) {
        @Override
        public int hashCode() {
            return 42;
        }
    }

    private ChainedHashTable<IssueId, ProjectTask> table;
    private ProjectTask firstTask;
    private ProjectTask secondTask;

    @BeforeEach
    void setUp() {
        table = new ChainedHashTable<>();
        firstTask = new ProjectTask(new TaskId("TASK-1"), "Write tests", TaskStatus.CREATED, 8);
        secondTask = new ProjectTask(new TaskId("TASK-2"), "Review code", TaskStatus.PENDING, 4);
    }

    @Test
    @DisplayName("Добавленное значение находится по ключу")
    void put_whenKeyIsNew_shouldStoreValue() {
        IssueId issueId = new IssueId(7, 42);

        assertNull(table.put(issueId, firstTask));
        assertSame(firstTask, table.get(issueId));
        assertTrue(table.containsKey(issueId));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("Два разных экземпляра логически равного ключа находят одно значение")
    void get_whenKeyIsAnotherEqualInstance_shouldReturnSameValue() {
        IssueId issueId = new IssueId(7, 42);
        IssueId sameIssueId = new IssueId(7, 42);
        table.put(issueId, firstTask);

        assertNotSame(issueId, sameIssueId);
        assertSame(firstTask, table.get(sameIssueId));
    }

    @Test
    @DisplayName("Повторное добавление по равному ключу заменяет значение, возвращает старое и не увеличивает размер")
    void put_whenKeyIsEqual_shouldReplaceValueAndKeepSize() {
        table.put(new IssueId(7, 42), firstTask);

        ProjectTask oldValue = table.put(new IssueId(7, 42), secondTask);

        assertSame(firstTask, oldValue);
        assertSame(secondTask, table.get(new IssueId(7, 42)));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("Неравные ключи с одинаковым hashCode хранят разные значения")
    void put_whenKeysCollide_shouldKeepBothValues() {
        IssueId first = new IssueId(1, 0);
        IssueId second = new IssueId(0, 31);
        table.put(first, firstTask);
        table.put(second, secondTask);

        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(2, table.size());
        assertSame(firstTask, table.get(first));
        assertSame(secondTask, table.get(second));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    @DisplayName("Удаление из начала, середины или конца цепочки не теряет соседние узлы")
    void remove_whenKeyIsInChain_shouldKeepOtherNodes(String removedName) {
        ChainedHashTable<FixedHashKey, Integer> chainTable = new ChainedHashTable<>();
        chainTable.put(new FixedHashKey("a"), 1);
        chainTable.put(new FixedHashKey("b"), 2);
        chainTable.put(new FixedHashKey("c"), 3);

        chainTable.remove(new FixedHashKey(removedName));

        assertEquals(2, chainTable.size());
        assertFalse(chainTable.containsKey(new FixedHashKey(removedName)));
        for (String name : new String[]{"a", "b", "c"}) {
            if (!name.equals(removedName)) {
                assertTrue(chainTable.containsKey(new FixedHashKey(name)));
            }
        }
    }

    @Test
    @DisplayName("Удаление возвращает удаленное значение")
    void remove_whenKeyExists_shouldReturnRemovedValue() {
        table.put(new IssueId(7, 42), firstTask);

        assertSame(firstTask, table.remove(new IssueId(7, 42)));
        assertNull(table.get(new IssueId(7, 42)));
        assertEquals(0, table.size());
    }

    @Test
    @DisplayName("Удаление отсутствующего ключа возвращает null и не меняет размер")
    void remove_whenKeyIsAbsent_shouldReturnNullAndKeepSize() {
        table.put(new IssueId(7, 42), firstTask);

        assertNull(table.remove(new IssueId(7, 43)));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("Поиск по отсутствующему ключу возвращает null и false")
    void get_whenKeyIsAbsent_shouldReturnNull() {
        table.put(new IssueId(7, 42), firstTask);

        assertNull(table.get(new IssueId(7, 43)));
        assertFalse(table.containsKey(new IssueId(7, 43)));
    }

    @Test
    @DisplayName("Пустая таблица имеет размер 0")
    void size_whenTableIsEmpty_shouldReturnZero() {
        assertEquals(0, table.size());
    }

    @Test
    @DisplayName("После расширения таблицы все значения по-прежнему находятся")
    void put_whenLoadFactorExceeded_shouldResizeAndKeepAllValues() {
        ChainedHashTable<IssueId, Integer> smallTable = new ChainedHashTable<>(2);
        for (int i = 0; i < 100; i++) {
            smallTable.put(new IssueId(1, i), i);
        }

        assertTrue(smallTable.capacity() > 2);
        assertEquals(100, smallTable.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, smallTable.get(new IssueId(1, i)));
        }
    }

    @Test
    @DisplayName("Таблица работает с другими типами ключей и значений")
    void put_whenOtherTypesUsed_shouldWork() {
        ChainedHashTable<String, Integer> wordCounts = new ChainedHashTable<>();
        wordCounts.put("java", 1);
        wordCounts.put("java", 2);

        assertEquals(2, wordCounts.get("java"));
        assertEquals(1, wordCounts.size());
    }

    @Test
    @DisplayName("При key = null бросаем исключение во всех операциях")
    void operations_whenKeyIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException putException
                = assertThrows(IllegalArgumentException.class, () -> table.put(null, firstTask));
        IllegalArgumentException getException
                = assertThrows(IllegalArgumentException.class, () -> table.get(null));
        IllegalArgumentException removeException
                = assertThrows(IllegalArgumentException.class, () -> table.remove(null));
        IllegalArgumentException containsException
                = assertThrows(IllegalArgumentException.class, () -> table.containsKey(null));
        assertEquals("key cannot be null", putException.getMessage());
        assertEquals("key cannot be null", getException.getMessage());
        assertEquals("key cannot be null", removeException.getMessage());
        assertEquals("key cannot be null", containsException.getMessage());
    }

    @Test
    @DisplayName("При value = null бросаем исключение")
    void put_whenValueIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> table.put(new IssueId(7, 42), null));
        assertEquals("value cannot be null", exception.getMessage());
        assertEquals(0, table.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("При неположительной начальной вместимости бросаем исключение")
    void constructor_whenInitialCapacityIsNotPositive_shouldThrowIllegalArgumentException(int capacity) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new ChainedHashTable<IssueId, ProjectTask>(capacity));
        assertEquals("initialCapacity must be positive", exception.getMessage());
    }
}
