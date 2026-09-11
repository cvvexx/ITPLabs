package edu.course.lab03;

import edu.course.lab03.discount.DiscountPolicy;
import edu.course.lab03.discount.NoDiscount;
import edu.course.lab03.discount.PercentDiscount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    @Test
    @DisplayName("При discountPolicy = null бросаем исключение")
    void constructor_whenDiscountPolicyIsNull_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
        assertEquals("discountPolicy cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Калькулятор без скидки возвращает исходную цену")
    void calculatePrice_whenPolicyIsNoDiscount_shouldReturnSamePrice() {
        PriceCalculator priceCalculator = new PriceCalculator(new NoDiscount());
        assertEquals(100, priceCalculator.calculatePrice(100), 1e-9);
    }

    @Test
    @DisplayName("Калькулятор со скидкой 20% для цены 100 возвращает 80")
    void calculatePrice_whenPolicyIsPercentDiscount20_shouldReturn80() {
        PriceCalculator priceCalculator = new PriceCalculator(new PercentDiscount(20));
        assertEquals(80, priceCalculator.calculatePrice(100), 1e-9);
    }

    @Test
    @DisplayName("Замена политики меняет результат без изменения калькулятора")
    void calculatePrice_whenPolicyIsReplaced_shouldUseNewPolicy() {
        DiscountPolicy policy = new NoDiscount();
        PriceCalculator priceCalculator = new PriceCalculator(policy);
        assertEquals(100, priceCalculator.calculatePrice(100), 1e-9);

        policy = new PercentDiscount(20);
        priceCalculator = new PriceCalculator(policy);
        assertEquals(80, priceCalculator.calculatePrice(100), 1e-9);
    }

    @Test
    @DisplayName("Калькулятор работает с любой реализацией DiscountPolicy")
    void calculatePrice_whenPolicyIsCustomImplementation_shouldUseIt() {
        DiscountPolicy fixedDiscount = price -> price - 10;
        PriceCalculator priceCalculator = new PriceCalculator(fixedDiscount);
        assertEquals(90, priceCalculator.calculatePrice(100), 1e-9);
    }
}
