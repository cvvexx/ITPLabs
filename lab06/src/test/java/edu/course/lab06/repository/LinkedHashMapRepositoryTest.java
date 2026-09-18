package edu.course.lab06.repository;

import edu.course.lab06.task.ProjectTask;
import edu.course.lab06.task.TaskId;
import edu.course.lab06.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapRepositoryTest {

    private LinkedHashMapRepository<TaskId, ProjectTask> repository;
    private ProjectTask first;
    private ProjectTask second;
    private ProjectTask third;

    @BeforeEach
    void setUp() {
        repository = new LinkedHashMapRepository<>();
        first = task("T-1", "Backend", TaskStatus.CREATED, 5);
        second = task("T-2", "Testing", TaskStatus.IN_PROGRESS, 8);
        third = task("T-3", "Analytics", TaskStatus.COMPLETED, 2);
    }

    @Test
    @DisplayName("Новый репозиторий пустой")
    void newRepository_whenNothingSaved_shouldBeEmpty() {
        assertEquals(0, repository.size());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("save сохраняет сущность по ее собственному id и возвращает ее же")
    void save_whenEntityIsNew_shouldStoreItByOwnId() {
        ProjectTask saved = repository.save(first);

        assertSame(first, saved);
        assertEquals(1, repository.size());
        assertEquals(Optional.of(first), repository.findById(new TaskId("T-1")));
    }

    @Test
    @DisplayName("save с уже занятым id бросает IllegalStateException")
    void save_whenIdAlreadyExists_shouldThrowIllegalStateException() {
        repository.save(first);
        ProjectTask duplicate = task("T-1", "Backend", TaskStatus.READY, 13);

        IllegalStateException exception
                = assertThrows(IllegalStateException.class, () -> repository.save(duplicate));
        assertEquals("entity with id TaskId[value=T-1] already exists", exception.getMessage());
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Поиск идет по значению id, а не по ссылке на объект id")
    void findById_whenAnotherIdInstanceWithSameValue_shouldFindEntity() {
        repository.save(first);
        TaskId sameValue = new TaskId("T-1");

        assertNotSame(first.getId(), sameValue);
        assertEquals(Optional.of(first), repository.findById(sameValue));
    }

    @Test
    @DisplayName("findById по неизвестному id возвращает пустой Optional")
    void findById_whenIdIsUnknown_shouldReturnEmptyOptional() {
        repository.save(first);
        assertEquals(Optional.empty(), repository.findById(new TaskId("T-99")));
    }

    @Test
    @DisplayName("findById(null) бросает исключение")
    void findById_whenIdIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
        assertEquals("id cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("findAll возвращает сущности в порядке запрошенных id")
    void findAll_whenIdsAreGiven_shouldReturnEntitiesInRequestedOrder() {
        repository.save(first);
        repository.save(second);
        repository.save(third);

        List<ProjectTask> found = repository.findAll(List.of(new TaskId("T-3"), new TaskId("T-1")));

        assertIterableEquals(List.of(third, first), found);
    }

    @Test
    @DisplayName("findAll пропускает неизвестные id")
    void findAll_whenSomeIdsAreUnknown_shouldSkipThem() {
        repository.save(first);
        repository.save(second);

        List<ProjectTask> found = repository.findAll(
                List.of(new TaskId("T-1"), new TaskId("T-99"), new TaskId("T-2")));

        assertEquals(2, found.size());
        assertIterableEquals(List.of(first, second), found);
    }

    @Test
    @DisplayName("findAll с пустым списком id возвращает пустой список")
    void findAll_whenIdsAreEmpty_shouldReturnEmptyList() {
        repository.save(first);
        assertTrue(repository.findAll(List.of()).isEmpty());
    }

    @Test
    @DisplayName("findAll(null) бросает исключение")
    void findAll_whenIdsAreNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> repository.findAll(null));
        assertEquals("ids cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("LinkedHashMap сохраняет порядок вставки")
    void findAll_whenEntitiesAreSaved_shouldKeepInsertionOrder() {
        repository.save(third);
        repository.save(first);
        repository.save(second);

        assertIterableEquals(List.of(third, first, second), repository.findAll());
    }

    @Test
    @DisplayName("update заменяет сущность с тем же id")
    void update_whenEntityExists_shouldReplaceIt() {
        repository.save(first);
        ProjectTask changed = task("T-1", "Backend", TaskStatus.COMPLETED, 21);

        ProjectTask updated = repository.update(changed);

        assertSame(changed, updated);
        assertEquals(1, repository.size());
        assertEquals(Optional.of(changed), repository.findById(new TaskId("T-1")));
        assertEquals(21, repository.findById(new TaskId("T-1")).orElseThrow().getEstimatedHours());
    }

    @Test
    @DisplayName("update не меняет порядок вставки")
    void update_whenEntityIsReplaced_shouldKeepItsPosition() {
        repository.save(first);
        repository.save(second);
        ProjectTask changed = task("T-1", "Backend", TaskStatus.COMPLETED, 21);

        repository.update(changed);

        assertIterableEquals(List.of(changed, second), repository.findAll());
    }

    @Test
    @DisplayName("update несуществующей сущности бросает NoSuchElementException")
    void update_whenEntityIsUnknown_shouldThrowNoSuchElementException() {
        NoSuchElementException exception
                = assertThrows(NoSuchElementException.class, () -> repository.update(first));
        assertEquals("entity with id TaskId[value=T-1] not found", exception.getMessage());
    }

    @Test
    @DisplayName("delete удаляет сущность по ее id")
    void delete_whenEntityExists_shouldRemoveIt() {
        repository.save(first);
        repository.save(second);

        repository.delete(first);

        assertEquals(1, repository.size());
        assertEquals(Optional.empty(), repository.findById(new TaskId("T-1")));
        assertIterableEquals(List.of(second), repository.findAll());
    }

    @Test
    @DisplayName("delete удаляет по id, даже если передан другой объект с тем же id")
    void delete_whenAnotherInstanceWithSameId_shouldRemoveStoredEntity() {
        repository.save(first);
        ProjectTask sameId = task("T-1", "Testing", TaskStatus.READY, 34);

        repository.delete(sameId);

        assertEquals(0, repository.size());
    }

    @Test
    @DisplayName("delete несуществующей сущности ничего не меняет")
    void delete_whenEntityIsUnknown_shouldDoNothing() {
        repository.save(first);

        repository.delete(second);

        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("save/update/delete с null бросают исключение")
    void methods_whenEntityIsNull_shouldThrowIllegalArgumentException() {
        assertEquals("entity cannot be null",
                assertThrows(IllegalArgumentException.class, () -> repository.save(null)).getMessage());
        assertEquals("entity cannot be null",
                assertThrows(IllegalArgumentException.class, () -> repository.update(null)).getMessage());
        assertEquals("entity cannot be null",
                assertThrows(IllegalArgumentException.class, () -> repository.delete(null)).getMessage());
    }

    @Test
    @DisplayName("Репозиторий работает с другим типом id и другой сущностью")
    void repository_whenAnotherIdType_shouldWorkTheSameWay() {
        LinkedHashMapRepository<Integer, User> users = new LinkedHashMapRepository<>();
        User user = new User(1, "Artem");

        users.save(user);

        assertEquals(Optional.of(user), users.findById(1));
        assertEquals(Optional.empty(), users.findById(2));
    }

    private static ProjectTask task(String id, String category, TaskStatus status, int estimatedHours) {
        return new ProjectTask(new TaskId(id), "title " + id, category, status, estimatedHours);
    }

    private record User(Integer id, String name) implements Identifiable<Integer> {

        @Override
        public Integer getId() {
            return id;
        }
    }
}
