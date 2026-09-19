package edu.course.lab07;

import edu.course.lab07.task.ProjectTask;
import edu.course.lab07.task.TaskId;
import edu.course.lab07.task.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DataAnalyticsTest {

    private static ProjectTask writeTests() {
        return new ProjectTask(
                new TaskId("TASK-1"), "Write tests", "Backend", TaskStatus.IN_PROGRESS, 8
        );
    }

    private static ProjectTask apiDocs() {
        return new ProjectTask(
                new TaskId("TASK-2"), "Api docs", "  backend ", TaskStatus.CREATED, 3
        );
    }

    private static ProjectTask fixBug() {
        return new ProjectTask(
                new TaskId("TASK-3"), "Fix bug", "Frontend", TaskStatus.IN_PROGRESS, 13
        );
    }

    private static ProjectTask deploy() {
        return new ProjectTask(
                new TaskId("TASK-4"), "Deploy", "DevOps", TaskStatus.COMPLETED, 5
        );
    }

    private static List<ProjectTask> sampleTasks() {
        return new ArrayList<>(List.of(writeTests(), apiDocs(), fixBug(), deploy()));
    }

    @Test
    @DisplayName("filterByStatus: пустой список дает пустой результат")
    public void filterByStatus_whenEmptyList_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.filterByStatus(List.of(), TaskStatus.CREATED));
    }

    @Test
    @DisplayName("filterByStatus: единственный подходящий элемент возвращается")
    public void filterByStatus_whenSingleMatchingTask_shouldReturnIt() {
        List<ProjectTask> tasks = List.of(writeTests());

        assertEquals(List.of(writeTests()), DataAnalytics.filterByStatus(tasks, TaskStatus.IN_PROGRESS));
    }

    @Test
    @DisplayName("filterByStatus: единственный неподходящий элемент отбрасывается")
    public void filterByStatus_whenSingleNotMatchingTask_shouldReturnEmptyList() {
        List<ProjectTask> tasks = List.of(writeTests());

        assertEquals(List.of(), DataAnalytics.filterByStatus(tasks, TaskStatus.COMPLETED));
    }

    @Test
    @DisplayName("filterByStatus: из нескольких статусов остаются только задачи нужного")
    public void filterByStatus_whenSeveralStatuses_shouldReturnOnlyRequestedStatus() {
        List<ProjectTask> result = DataAnalytics.filterByStatus(sampleTasks(), TaskStatus.IN_PROGRESS);

        assertEquals(List.of(writeTests(), fixBug()), result);
    }

    @Test
    @DisplayName("filterByStatus: если задач с таким статусом нет, результат пустой")
    public void filterByStatus_whenNoTaskWithStatus_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.filterByStatus(sampleTasks(), TaskStatus.READY));
    }

    @Test
    @DisplayName("getNormalizedCategories: пустой список дает пустой результат")
    public void getNormalizedCategories_whenEmptyList_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.getNormalizedCategories(List.of()));
    }

    @Test
    @DisplayName("getNormalizedCategories: одна задача дает одну нормализованную категорию")
    public void getNormalizedCategories_whenSingleTask_shouldReturnSingleCategory() {
        assertEquals(List.of("backend"), DataAnalytics.getNormalizedCategories(List.of(writeTests())));
    }

    @Test
    @DisplayName("getNormalizedCategories: регистр и пробелы схлопываются, результат отсортирован")
    public void getNormalizedCategories_whenCategoriesDifferByCaseAndSpaces_shouldReturnSortedDistinct() {
        List<String> result = DataAnalytics.getNormalizedCategories(sampleTasks());

        assertEquals(List.of("backend", "devops", "frontend"), result);
    }

    @Test
    @DisplayName("countByCategory: пустой список дает пустую карту")
    public void countByCategory_whenEmptyList_shouldReturnEmptyMap() {
        assertEquals(Map.of(), DataAnalytics.countByCategory(List.of()));
    }

    @Test
    @DisplayName("countByCategory: одна задача дает одну группу")
    public void countByCategory_whenSingleTask_shouldReturnSingleGroup() {
        assertEquals(Map.of("backend", 1L), DataAnalytics.countByCategory(List.of(writeTests())));
    }

    @Test
    @DisplayName("countByCategory: несколько групп, категории с разным написанием считаются одной")
    public void countByCategory_whenSeveralGroups_shouldCountNormalizedCategories() {
        Map<String, Long> result = DataAnalytics.countByCategory(sampleTasks());

        assertEquals(Map.of("backend", 2L, "frontend", 1L, "devops", 1L), result);
    }

    @Test
    @DisplayName("findMaxEstimatedHoursTask: на пустом списке возвращается Optional.empty")
    public void findMaxEstimatedHoursTask_whenEmptyList_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), DataAnalytics.findMaxEstimatedHoursTask(List.of()));
    }

    @Test
    @DisplayName("findMaxEstimatedHoursTask: единственная задача и есть максимум")
    public void findMaxEstimatedHoursTask_whenSingleTask_shouldReturnIt() {
        Optional<ProjectTask> result = DataAnalytics.findMaxEstimatedHoursTask(List.of(writeTests()));

        assertTrue(result.isPresent());
        assertEquals(writeTests(), result.orElseThrow());
    }

    @Test
    @DisplayName("findMaxEstimatedHoursTask: из нескольких задач выбирается самая трудоемкая")
    public void findMaxEstimatedHoursTask_whenSeveralTasks_shouldReturnMostExpensive() {
        Optional<ProjectTask> result = DataAnalytics.findMaxEstimatedHoursTask(sampleTasks());

        assertTrue(result.isPresent());
        assertEquals(fixBug(), result.orElseThrow());
    }

    @Test
    @DisplayName("sortTasksByTwoKeys: пустой список дает пустой результат")
    public void sortTasksByTwoKeys_whenEmptyList_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.sortTasksByTwoKeys(List.of()));
    }

    @Test
    @DisplayName("sortTasksByTwoKeys: один элемент возвращается как есть")
    public void sortTasksByTwoKeys_whenSingleTask_shouldReturnIt() {
        assertEquals(List.of(writeTests()), DataAnalytics.sortTasksByTwoKeys(List.of(writeTests())));
    }

    @Test
    @DisplayName("sortTasksByTwoKeys: при равной категории порядок задает название")
    public void sortTasksByTwoKeys_whenCategoriesAreEqual_shouldSortByTitle() {
        List<ProjectTask> tasks = List.of(writeTests(), apiDocs());

        assertEquals(List.of(apiDocs(), writeTests()), DataAnalytics.sortTasksByTwoKeys(tasks));
    }

    @Test
    @DisplayName("sortTasksByTwoKeys: сначала категория, потом название")
    public void sortTasksByTwoKeys_whenSeveralCategories_shouldSortByCategoryThenTitle() {
        List<ProjectTask> result = DataAnalytics.sortTasksByTwoKeys(sampleTasks());

        assertEquals(List.of(apiDocs(), writeTests(), deploy(), fixBug()), result);
    }

    @Test
    @DisplayName("getTopNCategories: пустой список дает пустой результат")
    public void getTopNCategories_whenEmptyList_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.getTopNCategories(List.of(), 3));
    }

    @Test
    @DisplayName("getTopNCategories: возвращается самая частая категория")
    public void getTopNCategories_whenNIsOne_shouldReturnMostFrequentCategory() {
        assertEquals(List.of("backend"), DataAnalytics.getTopNCategories(sampleTasks(), 1));
    }

    @Test
    @DisplayName("getTopNCategories: при равном числе задач категории идут по алфавиту")
    public void getTopNCategories_whenCountsAreEqual_shouldSortByCategoryName() {
        List<String> result = DataAnalytics.getTopNCategories(sampleTasks(), 3);

        assertEquals(List.of("backend", "devops", "frontend"), result);
    }

    @Test
    @DisplayName("getTopNCategories: n = 0 дает пустой результат")
    public void getTopNCategories_whenNIsZero_shouldReturnEmptyList() {
        assertEquals(List.of(), DataAnalytics.getTopNCategories(sampleTasks(), 0));
    }

    @Test
    @DisplayName("getTopNCategories: n больше числа категорий возвращает все категории")
    public void getTopNCategories_whenNIsGreaterThanCategoryCount_shouldReturnAllCategories() {
        assertEquals(List.of("backend", "devops", "frontend"), DataAnalytics.getTopNCategories(sampleTasks(), 10));
    }

    @Test
    @DisplayName("getTopNCategories: при отрицательном n бросаем исключение")
    public void getTopNCategories_whenNIsNegative_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class,
                () -> DataAnalytics.getTopNCategories(sampleTasks(), -1));

        assertEquals("n must not be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Входной список не изменяется ни одним методом аналитики")
    public void analytics_whenCalled_shouldNotModifyInputList() {
        List<ProjectTask> tasks = sampleTasks();

        DataAnalytics.filterByStatus(tasks, TaskStatus.IN_PROGRESS);
        DataAnalytics.getNormalizedCategories(tasks);
        DataAnalytics.countByCategory(tasks);
        DataAnalytics.findMaxEstimatedHoursTask(tasks);
        DataAnalytics.sortTasksByTwoKeys(tasks);
        DataAnalytics.getTopNCategories(tasks, 2);

        assertEquals(sampleTasks(), tasks);
    }
}
