package ru.javaops.masterjava.matrix;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] multiplyWithExecutorService(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < matrix1.length; i++) {
            final int iCopy = i;
            Callable<Boolean> current = () -> {
                for (int y = 0; y < matrix1.length; y++) {
                    int resultInt = 0;
                    for (int k = 0; k < matrix1.length; k++) {
                        resultInt += matrix1[iCopy][k] * matrix2[k][y];
                    }
                    result[iCopy][y] = resultInt;
                }
                return Boolean.TRUE;
            };
            tasks.add(current);
        }
        final ExecutorService executor = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER - 1);

        try {
            List<Future<Boolean>> dones = executor.invokeAll(tasks);
            for (Future<Boolean> done : dones) {
                done.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
        return result;
    }

    public static int[][] multiplyWithOnlyJava14(int[][] matrix1, int[][] matrix2) {

        final IncrementHelper helper = new IncrementHelper();
        int[][] result = new int[matrix1.length][matrix1.length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < MainMatrix.THREAD_NUMBER - 1; i++) {
            Thread worker = new Thread(() -> {
                while (helper.haveMoreWork() && !Thread.currentThread().isInterrupted()) {
                    Pair<Integer, Integer> current = helper.getAndIncrement();
                    final int x = current.getKey();
                    final int y = current.getValue();
                    int sum = 0;
                    for (int k = 0; k < matrix1.length; k++) {
                        sum += matrix1[x][k] * matrix2[k][y];
                    }
                    result[x][y] = sum;
                }
            });
            threads.add(worker);
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

    public static int[][] multiplyWitnStrassenAlgorithm(int[][] matrix1, int[][] matrix2) {

        final int[][] m1 = getQuadraticMatrixWithSizeDegreeOfTwo(matrix1);
        final int[][] m2 = getQuadraticMatrixWithSizeDegreeOfTwo(matrix2);

        final ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new Multiply(m1, m2));
    }

    public static boolean checkOnEqualsMatrixs(int[][] matrix1, int[][] matrix2) {
        for (int i = 0; i < matrix1.length; i++) {
            if (!Arrays.equals(matrix1[i], matrix2[i])) {
                return false;
            }
        }
        return true;
    }

    public static void fillMatrixWithRandomNumbers(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = (int) (Math.random() * 200);
            }
        }
    }

    private static int[][] getQuadraticMatrixWithSizeDegreeOfTwo(int[][] param) {
        if (isDegreeOfTwo(param.length)) {
            return param;
        } else {
            int two = 2;
            while (two < param.length) {
                two = two << 1;
            }
            int[][] result = new int[two][two];
            for (int i = 0; i < param.length; i++) {
                System.arraycopy(param[i], 0, result[i], 0, param.length);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        int[][] arr = new int[100][100];
        fillMatrixWithRandomNumbers(arr);
        int[][] moreArr = getQuadraticMatrixWithSizeDegreeOfTwo(arr);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("+++++++++++++++++++");
        for (int i = 0; i < moreArr.length; i++) {
            for (int j = 0; j < moreArr.length; j++) {
                System.out.print(moreArr[i][j] + " ");
            }
            System.out.println();
        }
    }

    static boolean isDegreeOfTwo(int number) {
        if (number == 0 || number == 1) {
            return false;
        }
        while (number > 1) {
            if (number % 2 != 0) {
                return false;
            }
            number = number >> 1;
        }
        return true;
    }

    static void printMatrix(int[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length; y++) {
                System.out.print(matrix[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("++++++++++++++++++++++++++");
    }
}
