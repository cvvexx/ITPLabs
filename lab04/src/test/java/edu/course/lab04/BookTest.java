package edu.course.lab04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book("1234", "title1");
        book2 = new Book("1234", "title2");
    }

    @Test
    @DisplayName("Значение, положенное по одной книге, находится по другой книге с тем же ISBN")
    void hashMap_whenGetByAnotherBookWithSameIsbn_shouldReturnSameValue() {
        Map<Book, String> shelves = new HashMap<>();
        shelves.put(book1, "A-1");

        assertNotSame(book1, book2);
        assertEquals("A-1", shelves.get(book2));
    }

    @Test
    @DisplayName("Книги равны по equals, если у них одинаковый ISBN, даже при разных названиях")
    void equals_whenSameIsbn_shouldReturnTrue() {
        assertEquals(book1, book2);
    }

    @Test
    @DisplayName("Книги разные по equals, если у них разный ISBN")
    void equals_whenDifferentIsbn_shouldReturnFalse() {
        Book otherBook = new Book("12345", "title1");
        assertNotEquals(book1, otherBook);
    }

    @Test
    @DisplayName("У книг с одинаковым ISBN одинаковый hashCode")
    void hashCode_whenSameIsbn_shouldReturnSameHashCode() {
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("При пустом isbn(null, пустая строка, пробелы) бросаем исключение")
    void constructor_whenIsbnIsNullOrBlank_shouldThrowIllegalArgumentException(String isbn) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new Book(isbn, "title1"));
        assertEquals("isbn cannot be null or blank", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("При пустом title(null, пустая строка, пробелы) бросаем исключение")
    void constructor_whenTitleIsNullOrBlank_shouldThrowIllegalArgumentException(String title) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new Book("1234", title));
        assertEquals("title cannot be null or blank", exception.getMessage());
    }
}
