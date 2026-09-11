package edu.course.lab01;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseToolkitTest {

    @Test
    void returnsTrueForEvenNumber() {
        boolean result = CourseToolkit.isEven(8);

        assertTrue(result);
    }

    @Test
    void returnsFalseForOddNumber() {
        boolean result = CourseToolkit.isEven(7);

        assertFalse(result);
    }

    @Test
    void returnsTrueForNegativeEvenNumber() {
        boolean result = CourseToolkit.isEven(-8);

        assertTrue(result);
    }


    @ParameterizedTest
    @ValueSource(ints = {2, 97, Integer.MAX_VALUE})
    void returnsTrueForPrime(int number) {
        assertTrue(CourseToolkit.isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, -7, 100, 49})
    void returnsFalseForNonPrime(int number) {
        assertFalse(CourseToolkit.isPrime(number));
    }


    @Test
    void returnsTrueForPalindromeWithOddLength() {
        assertTrue(CourseToolkit.isPalindrome("level"));
    }

    @Test
    void returnsTrueForPalindromeWithEvenLength() {
        assertTrue(CourseToolkit.isPalindrome("abba"));
    }

    @Test
    void returnsFalseForNonPalindrome() {
        assertFalse(CourseToolkit.isPalindrome("java"));
    }

    @Test
    void returnsTrueForEmptyAndSingleCharString() {
        assertTrue(CourseToolkit.isPalindrome(""));
        assertTrue(CourseToolkit.isPalindrome("x"));
    }

    @Test
    void comparesCharactersCaseSensitively() {
        assertFalse(CourseToolkit.isPalindrome("Level"));
    }

    @Test
    void throwsForNullText() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.isPalindrome(null));
    }


    @Test
    void returnsAverageOfPositiveNumbers() {
        assertEquals(2.5, CourseToolkit.average(new int[]{1, 2, 3, 4}), 1e-9);
    }

    @Test
    void returnsAverageOfNegativeNumbers() {
        assertEquals(-4.0, CourseToolkit.average(new int[]{-2, -4, -6}), 1e-9);
    }

    @Test
    void returnsAverageOfMixedNumbers() {
        assertEquals(0.0, CourseToolkit.average(new int[]{-5, 5, -10, 10}), 1e-9);
    }

    @Test
    void doesNotOverflowOnLargeValues() {
        int[] values = {Integer.MAX_VALUE, Integer.MAX_VALUE};

        assertEquals(Integer.MAX_VALUE, CourseToolkit.average(values), 1e-9);
    }

    @Test
    void throwsForEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.average(new int[0]));
    }

    @Test
    void throwsForNullArray() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.average(null));
    }
}
