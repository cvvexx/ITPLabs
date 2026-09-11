package edu.course.lab03;

import edu.course.lab03.discount.PercentDiscount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PercentDiscountTest {

    @Test
    @DisplayName("Скидка 20% для цены 100 дает 80")
    void apply_whenDiscountIs20AndPriceIs100_shouldReturn80() {
        PercentDiscount percentDiscount = new PercentDiscount(20);
        assertEquals(80, percentDiscount.apply(100), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({"0, 100", "100, 0"})
    @DisplayName("Граничные значения скидки(0 и 100) применяются корректно")
    void apply_whenDiscountIsBoundary_shouldReturnExpectedPrice(int discount, double expected) {
        PercentDiscount percentDiscount = new PercentDiscount(discount);
        assertEquals(expected, percentDiscount.apply(100), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    @DisplayName("При скидке вне диапазона от 0 до 100 бросаем исключение")
    void constructor_whenDiscountIsOutOfRange_shouldThrowIllegalArgumentException(int discount) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(discount));
        assertEquals("discount must be between 0 and 100", exception.getMessage());
    }

    @Test
    @DisplayName("При отрицательной цене бросаем исключение")
    void apply_whenPriceIsNegative_shouldThrowIllegalArgumentException() {
        PercentDiscount percentDiscount = new PercentDiscount(20);
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> percentDiscount.apply(-100));
        assertEquals("price cannot be negative", exception.getMessage());
    }
}
