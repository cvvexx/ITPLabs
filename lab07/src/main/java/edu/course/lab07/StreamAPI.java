package edu.course.lab07;

import java.util.Comparator;
import java.util.List;

public class StreamAPI {

    public static List<String> testStreamAPI(List<String> list) {
        return list.stream()
                .filter(str -> !str.isBlank())
                .map(String::toUpperCase)
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .toList();
    }

}
