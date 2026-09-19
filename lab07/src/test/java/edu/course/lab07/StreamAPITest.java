package edu.course.lab07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamAPITest {

    @Test
    @DisplayName("Возвращается правильно отсортированный список по заданию")
    public void testStreamAPI_whenTaskList_shouldReturnRightOrder() {
        List<String> list = new ArrayList<>(List.of("java", "", "git", "maven", "api"));
        var result = StreamAPI.testStreamAPI(list);

        assertEquals(List.of("API", "GIT", "JAVA", "MAVEN"), result);
        assertEquals(List.of("java", "", "git", "maven", "api"), list);
    }

    @Test
    @DisplayName("Длина важнее алфавита: сортировка по алфавиту идет только внутри одной длины")
    public void testStreamAPI_whenLengthAndAlphabetDisagree_shouldSortByLengthFirst() {
        List<String> list = new ArrayList<>(List.of("zip", "aaaa", "api", "b"));

        var result = StreamAPI.testStreamAPI(list);

        assertEquals(List.of("B", "API", "ZIP", "AAAA"), result);
    }

}