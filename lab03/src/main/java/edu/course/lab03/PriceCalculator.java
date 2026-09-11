package edu.course.lab03;

import edu.course.lab03.discount.DiscountPolicy;

public class PriceCalculator {

    private final DiscountPolicy discountPolicy;

    public PriceCalculator(DiscountPolicy discountPolicy) {
        if (discountPolicy == null) {
            throw new IllegalArgumentException("discountPolicy cannot be null");
        }
        this.discountPolicy = discountPolicy;
    }

    public double calculatePrice(double price) {
        return discountPolicy.apply(price);
    }
}
