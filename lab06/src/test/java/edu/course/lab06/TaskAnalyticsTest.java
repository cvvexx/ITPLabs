package edu.course.lab06;

import edu.course.lab06.task.ProjectTask;
import edu.course.lab06.task.TaskId;
import edu.course.lab06.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskAnalyticsTest {

    private TaskAnalytics analytics;

    @BeforeEach
    void setUp() {
        analytics = new TaskAnalytics(List.of(
                task("T-3", "Backend", TaskStatus.IN_PROGRESS, 5),
                task("T-1", "Backend", TaskStatus.COMPLETED, 8),
                task("T-5", "Testing", TaskStatus.CREATED, 2),
                task("T-2", "Analytics", TaskStatus.IN_PROGRESS, 5),
                task("T-4", "Backend", TaskStatus.COMPLETED, 13),
                task("T-6", "Testing", TaskStatus.READY, 2)
        ));
    }

    @Test
    @DisplayName("Число задач по статусам: CREATED=1, READY=1, IN_PROGRESS=2, COMPLETED=2")
    void countByStatus_whenTasksHaveDifferentStatuses_shouldCountEachStatus() {
        Map<TaskStatus, Integer> counts = analytics.countByStatus();

        assertEquals(4, counts.size());
        assertEquals(1, counts.get(TaskStatus.CREATED));
        assertEquals(1, counts.get(TaskStatus.READY));
        assertEquals(2, counts.get(TaskStatus.IN_PROGRESS));
        assertEquals(2, counts.get(TaskStatus.COMPLETED));
    }

    @Test
    @DisplayName("Статусы, которых нет среди задач, отсутствуют в Map")
    void countByStatus_whenStatusIsNotUsed_shouldNotBePresentInMap() {
        TaskAnalytics onlyCreated = new TaskAnalytics(List.of(
                task("T-1", "Backend", TaskStatus.CREATED, 3),
                task("T-2", "Backend", TaskStatus.CREATED, 4)
        ));

        Map<TaskStatus, Integer> counts = onlyCreated.countByStatus();

        assertEquals(Map.of(TaskStatus.CREATED, 2), counts);
        assertFalse(counts.containsKey(TaskStatus.COMPLETED));
        assertNull(counts.get(TaskStatus.IN_PROGRESS));
    }

    @Test
    @DisplayName("Для пустого списка задач Map пустая")
    void countByStatus_whenNoTasks_shouldReturnEmptyMap() {
        assertTrue(new TaskAnalytics(List.of()).countByStatus().isEmpty());
    }

    @Test
    @DisplayName("Уникальные категории: Analytics, Backend, Testing")
    void uniqueCategories_whenCategoriesRepeat_shouldReturnThemOnce() {
        Set<String> categories = analytics.uniqueCategories();

        assertEquals(3, categories.size());
        assertEquals(Set.of("Analytics", "Backend", "Testing"), categories);
    }

    @Test
    @DisplayName("Категории отсортированы по алфавиту")
    void uniqueCategories_whenCategoriesAreCollected_shouldBeSortedAlphabetically() {
        assertIterableEquals(List.of("Analytics", "Backend", "Testing"), analytics.uniqueCategories());
    }

    @Test
    @DisplayName("Для пустого списка задач Set пустой")
    void uniqueCategories_whenNoTasks_shouldReturnEmptySet() {
        assertTrue(new TaskAnalytics(List.of()).uniqueCategories().isEmpty());
    }

    @Test
    @DisplayName("Список отсортирован по трудоемкости, при равной трудоемкости — по идентификатору")
    void sortedByEffortAndId_whenTasksAreSorted_shouldOrderByHoursThenById() {
        List<ProjectTask> sorted = analytics.sortedByEffortAndId();

        assertEquals(6, sorted.size());
        assertIterableEquals(List.of("T-5", "T-6", "T-2", "T-3", "T-1", "T-4"), ids(sorted));
        assertIterableEquals(List.of(2, 2, 5, 5, 8, 13), hours(sorted));
    }

    @Test
    @DisplayName("Сортировка возвращает новый список и не меняет исходные данные")
    void sortedByEffortAndId_whenCalled_shouldReturnNewListAndKeepOriginalOrder() {
        List<ProjectTask> sorted = analytics.sortedByEffortAndId();
        sorted.clear();

        assertEquals(6, analytics.size());
        assertIterableEquals(List.of("T-5", "T-6", "T-2", "T-3", "T-1", "T-4"),
                ids(analytics.sortedByEffortAndId()));
    }

    @Test
    @DisplayName("Для пустого списка задач сортировка возвращает пустой список")
    void sortedByEffortAndId_whenNoTasks_shouldReturnEmptyList() {
        assertTrue(new TaskAnalytics(List.of()).sortedByEffortAndId().isEmpty());
    }

    @Test
    @DisplayName("Суммарная трудоемкость всех задач равна 35")
    void totalEstimatedHours_whenTasksAreGiven_shouldReturnSumOfHours() {
        assertEquals(35, analytics.totalEstimatedHours());
        assertEquals(0, new TaskAnalytics(List.of()).totalEstimatedHours());
    }

    @Test
    @DisplayName("Аналитика копирует список задач: изменение исходного списка ее не затрагивает")
    void constructor_whenSourceListIsModified_shouldNotAffectAnalytics() {
        List<ProjectTask> source = new ArrayList<>();
        source.add(task("T-1", "Backend", TaskStatus.CREATED, 3));
        TaskAnalytics copied = new TaskAnalytics(source);

        source.add(task("T-2", "Testing", TaskStatus.COMPLETED, 4));

        assertEquals(1, copied.size());
        assertEquals(Map.of(TaskStatus.CREATED, 1), copied.countByStatus());
        assertEquals(Set.of("Backend"), copied.uniqueCategories());
    }

    @Test
    @DisplayName("При null вместо списка задач бросаем исключение")
    void constructor_whenTasksIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new TaskAnalytics(null));
        assertEquals("tasks cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("При null среди задач бросаем исключение")
    void constructor_whenTasksContainNull_shouldThrowIllegalArgumentException() {
        List<ProjectTask> source = new ArrayList<>();
        source.add(task("T-1", "Backend", TaskStatus.CREATED, 3));
        source.add(null);

        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new TaskAnalytics(source));
        assertEquals("tasks cannot contain null", exception.getMessage());
    }

    private static ProjectTask task(String id, String category, TaskStatus status, int estimatedHours) {
        return new ProjectTask(new TaskId(id), "title " + id, category, status, estimatedHours);
    }

    private static List<String> ids(List<ProjectTask> tasks) {
        List<String> ids = new ArrayList<>();
        for (ProjectTask task : tasks) {
            ids.add(task.getId().value());
        }
        return ids;
    }

    private static List<Integer> hours(List<ProjectTask> tasks) {
        List<Integer> hours = new ArrayList<>();
        for (ProjectTask task : tasks) {
            hours.add(task.getEstimatedHours());
        }
        return hours;
    }
}
