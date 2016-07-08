package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DVarygin on 08.07.2016.
 */
public class Matrix14JavaMultiplier {

    volatile int i = 0;

    public int[][] multiply(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < MainMatrix.THREAD_NUMBER - 1; i++) {
            new Thread(() -> {

            });
        }

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }
}
