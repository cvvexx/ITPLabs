package edu.course.lab03.discount;

public class PercentDiscount implements DiscountPolicy{

    private final int discount;

    public PercentDiscount(int discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("discount must be between 0 and 100");
        }
        this.discount = discount;
    }

    @Override
    public double apply(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        return price * (100 - discount) / 100;
    }
}
