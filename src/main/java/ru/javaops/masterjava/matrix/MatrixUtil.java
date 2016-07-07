package ru.javaops.masterjava.matrix;

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
                if (!done.get()) {
                    throw new RuntimeException("Computation wasn't performed");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
        return result;
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
}
