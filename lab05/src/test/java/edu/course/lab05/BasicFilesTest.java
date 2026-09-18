package edu.course.lab05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicFilesTest {

    @TempDir
    Path tempDir;

    Path file;

    @BeforeEach
    public void setUp() throws IOException {
        file = tempDir.resolve("numbers.txt");
        Files.write(file, List.of("10", "bad", "20"), StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Читаем все строки из временного файла")
    public void readsAllLines() throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(List.of("10", "bad", "20"), lines);
    }

    @Test
    @DisplayName("Парсим строки в int и если встречаем строку bad, то ловим исключение NumberFormatException")
    public void parseInt_whenTryingParseStringToInt_shouldThrowsNumberFormatException() throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Integer> numbers = new ArrayList<>();

        assertThrows(
                NumberFormatException.class,
                () -> lines.forEach((line) -> numbers.add(Integer.parseInt(line)))
                );

        assertEquals(List.of(10), numbers);
    }

}
