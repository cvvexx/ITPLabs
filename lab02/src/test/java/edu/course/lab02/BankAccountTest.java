package edu.course.lab02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount(1000);
    }

    @Test
    @DisplayName("На баланс добавляются деньги")
    void deposit_whenAdded100_amountShouldBe1100() {
        bankAccount.deposit(100);
        assertEquals(1100, bankAccount.getBalance());
    }

    @Test
    @DisplayName("При попытке положить отрицательную сумму бросаем исключение")
    void deposit_whenAddedNegativeAmount_shouldThrowsIllegalArgumentException() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(-100));
        assertEquals("amount cannot be negative", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0})
    @DisplayName("При попытке снять невалидную сумму(<=0) со счета бросаем исключение")
    void withdraw_whenTryingToWithdrawInvalidAmount_shouldThrowIllegalArgumentException(int number) {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(number));
        assertEquals("amount must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Деньги снимаются с баланса")
    void withdraw_whenTryingToWithdrawPositive_shouldWithdrawAmountFromBalance() {
        bankAccount.withdraw(100);
        assertEquals(900, bankAccount.getBalance());
    }

    @Test
    @DisplayName("При попытке снять > чем есть на балансе, бросаем исключение")
    void withdraw_whenTryingToWithdrawAmountMoreThenBalance_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(1100));
        assertEquals("amount cannot be greater than balance", exception.getMessage());
    }
}