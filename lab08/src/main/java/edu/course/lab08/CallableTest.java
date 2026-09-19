package edu.course.lab08;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableTest {

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    public static int[] callableTest() throws ExecutionException, InterruptedException {
        try {
            Future<Integer> firstCallable = EXECUTOR_SERVICE.submit(() -> 10);
            Future<Integer> secondCallable = EXECUTOR_SERVICE.submit(() -> 20);
            Future<Integer> thirdCallable = EXECUTOR_SERVICE.submit(() -> 30);

            return new int[]{firstCallable.get(), secondCallable.get(), thirdCallable.get()};
        } finally {
            EXECUTOR_SERVICE.shutdown();
        }
    }

}
