package ru.javaops.masterjava.matrix;

import java.util.concurrent.RecursiveTask;

/**
 * Created by DVarygin on 12.07.2016.
 */
public class MyFork extends RecursiveTask<int[][]> {

    private int[][] big1;

    private int[][] big2;

    public MyFork(int[][] big1, int[][] big2) {
        this.big1 = big1;
        this.big2 = big2;
    }

    @Override
    protected int[][] compute() {
        if (big1.length == 2) {
            return multiplyInts(big1, big2);
        } else {
            Four four1 = getFourFromMatrix(big1);
            Four four2 = getFourFromMatrix(big2);

            return multiplyMatrix(four1, four2);
        }
    }

    private int[][] multiplyMatrix(Four four1, Four four2) {

        return null;
    }

    private int[][] multiplyInts(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                int sum = 0;
                for (int k = 0; k < matrix1.length; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    private int[][] sumMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    private int[][] subtract(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return result;
    }

    private Four getFourFromMatrix(int[][] big) {
        int newSize = big.length >> 1;
        int[][] matrix1 = new int[newSize][newSize];
        int[][] matrix2 = new int[newSize][newSize];
        int[][] matrix3 = new int[newSize][newSize];
        int[][] matrix4 = new int[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            System.arraycopy(big[i], 0, matrix1[i], 0, newSize);
        }
        for (int i = newSize; i < big.length; i++) {
            System.arraycopy(big[i], 0, matrix2[i], 0, newSize);
        }
        for (int i = 0; i < newSize; i++) {
            System.arraycopy(big[i], newSize, matrix3[i], newSize, big.length - newSize);
        }
        for (int i = newSize; i < big.length; i++) {
            System.arraycopy(big[i], newSize, matrix4[i], newSize, big.length - newSize);
        }
        return new Four(matrix1, matrix2, matrix3, matrix4);
    }

    private static class Four {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int[][] matrix3;
        private final int[][] matrix4;

        private Four(int[][] matrix1, int[][] matrix2, int[][] matrix3, int[][] matrix4) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.matrix3 = matrix3;
            this.matrix4 = matrix4;
        }
    }
}
