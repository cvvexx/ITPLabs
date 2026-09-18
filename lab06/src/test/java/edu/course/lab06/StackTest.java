package edu.course.lab06;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    private Stack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new Stack<>();
    }

    @Test
    @DisplayName("Новый стек пустой и имеет нулевой размер")
    void newStack_whenNothingPushed_shouldBeEmpty() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("После push стек не пустой и размер увеличивается")
    void push_whenValuesAreAdded_shouldIncreaseSize() {
        stack.push("first");
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());

        stack.push("second");
        assertEquals(2, stack.size());
    }

    @Test
    @DisplayName("pop возвращает элементы в порядке LIFO")
    void pop_whenSeveralValuesArePushed_shouldReturnThemInLifoOrder() {
        stack.push("first");
        stack.push("second");
        stack.push("third");

        assertEquals("third", stack.pop());
        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
    }

    @Test
    @DisplayName("pop удаляет элемент и уменьшает размер")
    void pop_whenCalled_shouldRemoveValue() {
        stack.push("first");
        stack.push("second");

        stack.pop();

        assertEquals(1, stack.size());
        assertEquals("first", stack.peek());
    }

    @Test
    @DisplayName("После извлечения всех элементов стек снова пустой")
    void pop_whenAllValuesAreRemoved_shouldMakeStackEmpty() {
        stack.push("first");
        stack.push("second");

        stack.pop();
        stack.pop();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("peek возвращает верхний элемент, не удаляя его")
    void peek_whenCalled_shouldReturnTopValueWithoutRemoving() {
        stack.push("first");
        stack.push("second");

        assertEquals("second", stack.peek());
        assertEquals("second", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    @DisplayName("pop на пустом стеке бросает NoSuchElementException")
    void pop_whenStackIsEmpty_shouldThrowNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    @DisplayName("peek на пустом стеке бросает NoSuchElementException")
    void peek_whenStackIsEmpty_shouldThrowNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    @Test
    @DisplayName("push(null) бросает исключение и не меняет стек")
    void push_whenValueIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> stack.push(null));

        assertEquals("value cannot be null", exception.getMessage());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("push(null) на непустом стеке не портит уже сохраненные элементы")
    void push_whenValueIsNullAndStackIsNotEmpty_shouldKeepStoredValues() {
        stack.push("first");

        assertThrows(IllegalArgumentException.class, () -> stack.push(null));

        assertEquals(1, stack.size());
        assertEquals("first", stack.peek());
    }

    @Test
    @DisplayName("Стек хранит дубликаты как отдельные элементы")
    void push_whenSameValueIsPushedTwice_shouldStoreBothCopies() {
        stack.push("value");
        stack.push("value");

        assertEquals(2, stack.size());
        assertEquals("value", stack.pop());
        assertEquals("value", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("Стек параметризуется разными типами и возвращает значение без приведения типов")
    void stack_whenDifferentTypeParameters_shouldReturnTypedValue() {
        Stack<Integer> numbers = new Stack<>();
        numbers.push(10);
        stack.push("ten");

        int number = numbers.pop();
        String text = stack.pop();

        assertEquals(10, number);
        assertEquals("ten", text);
    }

    @Test
    @DisplayName("Разные стеки не делят между собой внутреннее хранилище")
    void stack_whenTwoInstances_shouldNotShareState() {
        Stack<String> other = new Stack<>();

        stack.push("first");

        assertEquals(1, stack.size());
        assertTrue(other.isEmpty());
    }
}
