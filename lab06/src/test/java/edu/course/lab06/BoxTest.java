package edu.course.lab06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    @Test
    @DisplayName("Коробка хранит значение, переданное в конструктор")
    void constructor_whenValueIsGiven_shouldStoreIt() {
        Box<String> box = new Box<>("hello");
        assertEquals("hello", box.getValue());
    }

    @Test
    @DisplayName("Коробка хранит ссылку на тот же объект, а не копию")
    void getValue_whenValueIsObject_shouldReturnSameReference() {
        List<Integer> numbers = List.of(1, 2, 3);
        Box<List<Integer>> box = new Box<>(numbers);
        assertSame(numbers, box.getValue());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "value"})
    @DisplayName("Коробка принимает любое значение, включая null и пустую строку")
    void constructor_whenValueIsNullOrBlank_shouldStoreItAsIs(String value) {
        Box<String> box = new Box<>(value);
        assertEquals(value, box.getValue());
    }

    @Test
    @DisplayName("setValue заменяет хранимое значение")
    void setValue_whenNewValueIsGiven_shouldReplaceOldValue() {
        Box<Integer> box = new Box<>(1);
        box.setValue(42);
        assertEquals(42, box.getValue());
    }

    @Test
    @DisplayName("setValue(null) очищает коробку")
    void setValue_whenValueIsNull_shouldStoreNull() {
        Box<Integer> box = new Box<>(1);
        box.setValue(null);
        assertNull(box.getValue());
    }

    @Test
    @DisplayName("Разные коробки хранят значения независимо друг от друга")
    void setValue_whenTwoBoxes_shouldNotAffectEachOther() {
        Box<String> first = new Box<>("first");
        Box<String> second = new Box<>("second");

        first.setValue("changed");

        assertEquals("changed", first.getValue());
        assertEquals("second", second.getValue());
    }

    @Test
    @DisplayName("Коробка параметризуется разными типами и возвращает значение без приведения типов")
    void box_whenDifferentTypeParameters_shouldReturnTypedValue() {
        Box<Integer> intBox = new Box<>(10);
        Box<String> stringBox = new Box<>("ten");

        //intBox.setValue("123") -> Ошибка компиляции из-за несоотвествия generic-типа

        int number = intBox.getValue();
        String text = stringBox.getValue();

        assertEquals(10, number);
        assertEquals("ten", text);
    }

    @Test
    @DisplayName("Параметр типа не дает положить число в Box<String>: ошибка ловится на компиляции")
    void box_whenWrongTypeIsUsed_shouldNotCompile() {
        Box<String> box = new Box<>("text");
        assertEquals("text", box.getValue());
    }

    @Test
    @DisplayName("Коробку можно положить в коробку")
    void box_whenNested_shouldReturnInnerBox() {
        Box<Box<String>> outer = new Box<>(new Box<>("inner"));
        assertEquals("inner", outer.getValue().getValue());
    }

    @Test
    @DisplayName("Коробка подтипа подходит под Box<? extends Number>")
    void box_whenUpperBoundedWildcard_shouldAcceptSubtypeBoxes() {
        assertEquals(1.0, sum(new Box<>(1), new Box<>(0.0)));
    }

    private static double sum(Box<? extends Number> first, Box<? extends Number> second) {
        return first.getValue().doubleValue() + second.getValue().doubleValue();
    }
}
