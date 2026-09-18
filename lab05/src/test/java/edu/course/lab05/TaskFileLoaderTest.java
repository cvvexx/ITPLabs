package edu.course.lab05;

import edu.course.lab05.task.ProjectTask;
import edu.course.lab05.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskFileLoaderTest {

    private static final String HEADER = "id;title;category;estimatedHours;status";

    @TempDir
    Path tempDir;

    TaskFileLoader loader;

    @BeforeEach
    public void setUp() {
        loader = new TaskFileLoader();
    }

    private Path writeFile(String name, String... lines) throws IOException {
        Path file = tempDir.resolve(name);
        Files.write(file, List.of(lines), StandardCharsets.UTF_8);
        return file;
    }

    @Test
    @DisplayName("Корректный файл разбирается без ошибок")
    public void load_whenFileIsValid_shouldReturnTasksWithoutErrors() throws IOException {
        Path file = writeFile("valid.csv",
                HEADER,
                "T-101;Prepare release;backend;8;READY",
                "T-102;Fix login;frontend;3;IN_PROGRESS");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.errors());
        assertEquals(2, result.tasks().size());

        ProjectTask first = result.tasks().get(0);
        assertEquals("T-101", first.getId().value());
        assertEquals("Prepare release", first.getTitle());
        assertEquals("backend", first.getCategory());
        assertEquals(8, first.getEstimatedHours());
        assertEquals(TaskStatus.READY, first.getStatus());

        ProjectTask second = result.tasks().get(1);
        assertEquals("T-102", second.getId().value());
        assertEquals(TaskStatus.IN_PROGRESS, second.getStatus());
    }

    @Test
    @DisplayName("Кириллица читается в кодировке UTF-8")
    public void load_whenFileContainsCyrillic_shouldKeepUnicodeText() throws IOException {
        Path file = writeFile("unicode.csv",
                HEADER,
                "T-201;Подготовить релиз;бэкенд;5;CREATED",
                "T-202;Проверить тесты ✓;тестирование;2;COMPLETED");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.errors());
        assertEquals("Подготовить релиз", result.tasks().get(0).getTitle());
        assertEquals("бэкенд", result.tasks().get(0).getCategory());
        assertEquals("Проверить тесты ✓", result.tasks().get(1).getTitle());
    }

    @Test
    @DisplayName("Пустое поле возвращается как ошибка записи")
    public void load_whenFieldIsEmpty_shouldReportValidationError() throws IOException {
        Path file = writeFile("empty-field.csv",
                HEADER,
                "T-301;;backend;4;READY");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.tasks());
        assertEquals(List.of(new ValidationError(2, "title", "value cannot be empty")), result.errors());
    }

    @Test
    @DisplayName("Неправильное число возвращается как ошибка записи")
    public void load_whenHoursAreNotANumber_shouldReportValidationError() throws IOException {
        Path file = writeFile("bad-number.csv",
                HEADER,
                "T-401;Prepare release;backend;eight;READY");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.tasks());
        assertEquals(List.of(new ValidationError(2, "estimatedHours", "'eight' is not a number")), result.errors());
    }

    @Test
    @DisplayName("Неположительное число часов не проходит предметную проверку")
    public void load_whenHoursAreNotPositive_shouldReportValidationError() throws IOException {
        Path file = writeFile("non-positive.csv",
                HEADER,
                "T-402;Prepare release;backend;0;READY");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.tasks());
        assertEquals(List.of(new ValidationError(2, "estimatedHours", "value must be positive but was 0")), result.errors());
    }

    @Test
    @DisplayName("Неизвестный статус возвращается как ошибка записи")
    public void load_whenStatusIsUnknown_shouldReportValidationError() throws IOException {
        Path file = writeFile("bad-status.csv",
                HEADER,
                "T-501;Prepare release;backend;8;ARCHIVED");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.tasks());
        assertEquals(List.of(new ValidationError(2, "status", "unknown status 'ARCHIVED'")), result.errors());
    }

    @Test
    @DisplayName("Неправильное число столбцов возвращается как ошибка записи")
    public void load_whenColumnCountIsWrong_shouldReportValidationError() throws IOException {
        Path file = writeFile("bad-columns.csv",
                HEADER,
                "T-601;Prepare release;backend;8",
                "T-602;Prepare release;backend;8;READY;extra");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.tasks());
        assertEquals(
                List.of(
                        new ValidationError(2, "row", "expected 5 fields but found 4"),
                        new ValidationError(3, "row", "expected 5 fields but found 6")),
                result.errors());
    }

    @Test
    @DisplayName("Корректные строки сохраняются вместе с ошибками остальных")
    public void load_whenFileHasValidAndInvalidRows_shouldKeepValidTasks() throws IOException {
        Path file = writeFile("mixed.csv",
                HEADER,
                "T-701;Prepare release;backend;8;READY",
                "T-702;Fix login;frontend;many;READY",
                "",
                "T-703;Write docs;docs;2;CREATED",
                "T-704;Broken;docs;2;ARCHIVED");

        LoadResult result = loader.load(file);

        assertEquals(2, result.tasks().size());
        assertEquals("T-701", result.tasks().get(0).getId().value());
        assertEquals("T-703", result.tasks().get(1).getId().value());

        assertTrue(result.hasErrors());
        assertEquals(
                List.of(
                        new ValidationError(3, "estimatedHours", "'many' is not a number"),
                        new ValidationError(6, "status", "unknown status 'ARCHIVED'")),
                result.errors());
    }

    @Test
    @DisplayName("Отсутствующий файл приводит к DataLoadingException с сохраненной причиной")
    public void load_whenFileIsMissing_shouldThrowDataLoadingExceptionWithCause() {
        Path missing = tempDir.resolve("missing.csv");

        DataLoadingException exception = assertThrows(
                DataLoadingException.class,
                () -> loader.load(missing));

        assertNotNull(exception.getCause());
        assertInstanceOf(NoSuchFileException.class, exception.getCause());
        assertTrue(exception.getMessage().contains("missing.csv"));
    }

    @Test
    @DisplayName("Неверный заголовок приводит к DataLoadingException")
    public void load_whenHeaderIsWrong_shouldThrowDataLoadingException() throws IOException {
        Path file = writeFile("bad-header.csv",
                "id;name;category;estimatedHours;status",
                "T-801;Prepare release;backend;8;READY");

        assertThrows(DataLoadingException.class, () -> loader.load(file));
    }

    @Test
    @DisplayName("Пустой файл приводит к DataLoadingException")
    public void load_whenFileIsEmpty_shouldThrowDataLoadingException() throws IOException {
        Path file = tempDir.resolve("empty.csv");
        Files.write(file, new byte[0]);

        assertThrows(DataLoadingException.class, () -> loader.load(file));
    }

    @Test
    @DisplayName("Поля в кавычках могут содержать разделитель")
    public void load_whenFieldIsQuoted_shouldKeepDelimiterInsideValue() throws IOException {
        Path file = writeFile("quoted.csv",
                HEADER,
                "T-901;\"Prepare release; then deploy\";backend;8;READY",
                "T-902;\"Say \"\"hello\"\" twice\";frontend;1;CREATED");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.errors());
        assertEquals("Prepare release; then deploy", result.tasks().get(0).getTitle());
        assertEquals("Say \"hello\" twice", result.tasks().get(1).getTitle());
    }

    @Test
    @DisplayName("Лишние пробелы вокруг значений обрезаются")
    public void load_whenValuesHaveSpaces_shouldTrimThem() throws IOException {
        Path file = writeFile("spaces.csv",
                HEADER,
                " T-1001 ; Prepare release ; backend ; 8 ; READY ");

        LoadResult result = loader.load(file);

        assertEquals(List.of(), result.errors());
        assertEquals("T-1001", result.tasks().get(0).getId().value());
        assertEquals("Prepare release", result.tasks().get(0).getTitle());
        assertEquals(TaskStatus.READY, result.tasks().get(0).getStatus());
    }
}
