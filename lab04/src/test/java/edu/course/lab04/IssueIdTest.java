package edu.course.lab04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class IssueIdTest {

    private IssueId issueId;

    @BeforeEach
    void setUp() {
        issueId = new IssueId(7, 42);
    }

    @Test
    @DisplayName("Разные экземпляры с одинаковыми кодом проекта и номером равны")
    void equals_whenSameProjectCodeAndValue_shouldReturnTrue() {
        IssueId sameIssueId = new IssueId(7, 42);
        assertNotSame(issueId, sameIssueId);
        assertEquals(issueId, sameIssueId);
    }

    @Test
    @DisplayName("У равных ключей одинаковый hashCode")
    void hashCode_whenKeysAreEqual_shouldReturnSameHashCode() {
        IssueId sameIssueId = new IssueId(7, 42);
        assertEquals(issueId.hashCode(), sameIssueId.hashCode());
    }

    @ParameterizedTest
    @CsvSource({"8, 42", "7, 43"})
    @DisplayName("Ключи не равны, если отличается код проекта или номер")
    void equals_whenProjectCodeOrValueDiffers_shouldReturnFalse(int projectCode, int value) {
        IssueId otherIssueId = new IssueId(projectCode, value);
        assertNotEquals(issueId, otherIssueId);
    }

    @Test
    @DisplayName("Сравнение с null возвращает false")
    void equals_whenComparedWithNull_shouldReturnFalse() {
        assertFalse(issueId.equals(null));
    }

    @Test
    @DisplayName("Сравнение с объектом другого типа возвращает false")
    void equals_whenComparedWithOtherType_shouldReturnFalse() {
        assertFalse(issueId.equals("7-42"));
    }

    @Test
    @DisplayName("Неравные ключи могут иметь одинаковый hashCode")
    void hashCode_whenKeysCollide_shouldNotMakeKeysEqual() {
        IssueId first = new IssueId(1, 0);
        IssueId second = new IssueId(0, 31);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, second);
    }
}
