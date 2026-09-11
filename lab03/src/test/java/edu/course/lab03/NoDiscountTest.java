package edu.course.lab03;

import edu.course.lab03.discount.NoDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NoDiscountTest {

    private NoDiscount noDiscount;

    @BeforeEach
    void setUp() {
        noDiscount = new NoDiscount();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 100, 99.99})
    @DisplayName("Без скидки возвращается исходная цена")
    void apply_whenPriceIsNotNegative_shouldReturnSamePrice(double price) {
        assertEquals(price, noDiscount.apply(price), 1e-9);
    }

    @Test
    @DisplayName("При отрицательной цене бросаем исключение")
    void apply_whenPriceIsNegative_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> noDiscount.apply(-100));
        assertEquals("price cannot be negative", exception.getMessage());
    }
}
