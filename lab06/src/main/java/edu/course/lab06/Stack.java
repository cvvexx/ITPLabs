package edu.course.lab06;

import java.util.ArrayList;
import java.util.List;

public final class Stack<T> {

    private final List<T> stack = new ArrayList<>();


    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        stack.add(value);
    }

    public T pop() {
        return stack.removeLast();//Под капотом выбрасывает NoSuchElementException
    }

    public T peek() {
        return stack.getLast();//тут тоже
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
